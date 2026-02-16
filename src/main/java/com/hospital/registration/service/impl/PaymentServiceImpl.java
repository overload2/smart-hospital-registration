package com.hospital.registration.service.impl;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.config.RabbitMQConfig;
import com.hospital.registration.dto.PaymentDTO;
import com.hospital.registration.dto.RefundMessage;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.mapper.PaymentMapper;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.service.PaymentService;
import com.hospital.registration.vo.PaymentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @title: PaymentServiceImpl
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 支付服务实现类
 */
@Slf4j
@Service
public class PaymentServiceImpl implements PaymentService {

    private final RabbitTemplate rabbitTemplate;
    private final PaymentMapper paymentMapper;
    private final RegistrationMapper registrationMapper;

    public PaymentServiceImpl(RabbitTemplate rabbitTemplate,
                              PaymentMapper paymentMapper,
                              RegistrationMapper registrationMapper) {
        this.rabbitTemplate = rabbitTemplate;
        this.paymentMapper = paymentMapper;
        this.registrationMapper = registrationMapper;
    }

    /**
     * 创建支付订单
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PaymentVO createPayment(PaymentDTO paymentDTO) {
        log.info("创建支付订单 - 挂号ID: {}, 支付方式: {}",
                paymentDTO.getRegistrationId(), paymentDTO.getPaymentMethod());

        // 查询挂号记录
        Registration registration = registrationMapper.selectById(paymentDTO.getRegistrationId());
        if (registration == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        // 检查是否已支付
        if (registration.getPaymentStatus() == com.hospital.registration.common.PaymentStatus.PAID) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该挂号已支付");
        }

        // 检查支付金额是否匹配
        if (paymentDTO.getAmount().compareTo(registration.getRegistrationFee()) != 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "支付金额不匹配");
        }

        // 检查是否已存在支付记录
        Payment existingPayment = paymentMapper.selectByRegistrationId(paymentDTO.getRegistrationId());
        if (existingPayment != null && existingPayment.getPaymentStatus() == PaymentStatus.PAID) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该挂号已有支付记录");
        }

        // 生成交易流水号
        String transactionNo = generateTransactionNo();

        // 创建支付记录
        Payment payment = new Payment();
        payment.setTransactionNo(transactionNo);
        payment.setRegistrationId(registration.getId());
        payment.setUserId(registration.getPatientId());
        payment.setAmount(paymentDTO.getAmount());
        payment.setPaymentMethod(paymentDTO.getPaymentMethod());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setRemark("挂号费支付");

        int result = paymentMapper.insert(payment);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "创建支付订单失败");
        }

        log.info("支付订单创建成功 - 交易流水号: {}", transactionNo);

        // 转换为VO返回
        return convertToVO(payment, registration);
    }

    /**
     * 处理支付回调（模拟）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean handlePaymentCallback(String transactionNo) {
        log.info("处理支付回调 - 交易流水号: {}", transactionNo);

        // 查询支付记录
        Payment payment = paymentMapper.selectByTransactionNo(transactionNo);
        if (payment == null) {
            log.warn("支付记录不存在 - 交易流水号: {}", transactionNo);
            return false;
        }

        // 检查支付状态
        if (payment.getPaymentStatus() == PaymentStatus.PAID) {
            log.warn("该订单已支付 - 交易流水号: {}", transactionNo);
            return true;
        }

        // 更新支付记录状态
        Payment updatePayment = new Payment();
        updatePayment.setId(payment.getId());
        updatePayment.setPaymentStatus(PaymentStatus.PAID);
        updatePayment.setPaymentTime(LocalDateTime.now());

        int result = paymentMapper.updateById(updatePayment);
        if (result <= 0) {
            log.error("更新支付状态失败 - 交易流水号: {}", transactionNo);
            return false;
        }

        // 更新挂号记录的支付状态
        Registration registration = new Registration();
        registration.setId(payment.getRegistrationId());
        registration.setPaymentStatus(com.hospital.registration.common.PaymentStatus.PAID);
        registration.setPaymentTime(LocalDateTime.now());

        registrationMapper.updateById(registration);

        log.info("支付回调处理成功 - 交易流水号: {}", transactionNo);
        return true;
    }

    /**
     * 根据挂号ID查询支付记录
     */
    @Override
    public PaymentVO getPaymentByRegistrationId(Long registrationId) {
        log.info("查询支付记录 - 挂号ID: {}", registrationId);

        Payment payment = paymentMapper.selectByRegistrationId(registrationId);
        if (payment == null) {
            return null;
        }

        Registration registration = registrationMapper.selectById(payment.getRegistrationId());
        return convertToVO(payment, registration);
    }

    /**
     * 根据交易流水号查询支付记录
     */
    @Override
    public PaymentVO getPaymentByTransactionNo(String transactionNo) {
        log.info("查询支付记录 - 交易流水号: {}", transactionNo);

        Payment payment = paymentMapper.selectByTransactionNo(transactionNo);
        if (payment == null) {
            return null;
        }

        Registration registration = registrationMapper.selectById(payment.getRegistrationId());
        return convertToVO(payment, registration);
    }

    /**
     * 查询用户的支付记录
     */
    @Override
    public List<PaymentVO> getUserPayments(Long userId) {
        log.info("查询用户支付记录 - 用户ID: {}", userId);

        List<Payment> payments = paymentMapper.selectByUserId(userId);
        return payments.stream().map(payment -> {
            Registration registration = registrationMapper.selectById(payment.getRegistrationId());
            return convertToVO(payment, registration);
        }).collect(Collectors.toList());
    }

    /**
     * 异步退款
     * @param registrationNo 挂号单号
     * @param amount 退款金额
     */
    @Override
    @Async
    public void refundAsync(String registrationNo, BigDecimal amount) {
        log.info("【异步退款】开始处理退款 - 挂号单号: {}, 退款金额: {}", registrationNo, amount);

        try {
            //查询挂号记录
            Registration registration = registrationMapper.selectByRegistrationNo(registrationNo);
            if (registration == null) {
                log.error("【异步退款】挂号记录不存在 - 挂号单号: {}", registrationNo);
                return;
            }
            //查询支付记录并更新为退款中
            Payment payment = paymentMapper.selectByRegistrationId(registration.getId());
            if(payment!=null){
                Payment updatePayment = new Payment();
                updatePayment.setId(payment.getId());
                updatePayment.setPaymentStatus(PaymentStatus.REFUNDING);
                paymentMapper.updateById(updatePayment);
            }
            // 构建退款消息
            RefundMessage message = new RefundMessage();
            message.setRegistrationId(registration.getId());
            message.setRegistrationNo(registrationNo);
            message.setAmount(amount);
            message.setPatientId(registration.getPatientId());

            // 发送到 RabbitMQ 队列
            rabbitTemplate.convertAndSend(
                    RabbitMQConfig.REFUND_EXCHANGE,
                    RabbitMQConfig.REFUND_ROUTING_KEY,
                    message
            );

            log.info("【异步退款】退款消息已发送到队列 - 挂号单号: {}", registrationNo);

        } catch (Exception e) {
            log.error("【异步退款】发送退款消息失败 - 挂号单号: {}, 错误: {}",
                    registrationNo, e.getMessage(), e);
        }
    }
    /**
     * 生成交易流水号
     * 格式: PAY + yyyyMMddHHmmss + 4位随机数
     */
    private String generateTransactionNo() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int random = (int) (Math.random() * 9000) + 1000;
        return "PAY" + timestamp + random;
    }

    /**
     * 转换为VO
     */
    private PaymentVO convertToVO(Payment payment, Registration registration) {
        PaymentVO vo = new PaymentVO();
        BeanUtils.copyProperties(payment, vo);

        if (registration != null) {
            vo.setRegistrationNo(registration.getRegistrationNo());
        }

        // 设置支付方式名称
        vo.setPaymentMethodName(getPaymentMethodName(payment.getPaymentMethod()));

        // 设置支付状态名称
        if (payment.getPaymentStatus() != null) {
            vo.setPaymentStatusName(payment.getPaymentStatus().getName());
        }

        return vo;
    }

    /**
     * 获取支付方式名称
     */
    private String getPaymentMethodName(String paymentMethod) {
        if (paymentMethod == null) {
            return null;
        }
        switch (paymentMethod) {
            case "WECHAT":
                return "微信支付";
            case "ALIPAY":
                return "支付宝";
            case "CASH":
                return "现金";
            default:
                return paymentMethod;
        }
    }
}


