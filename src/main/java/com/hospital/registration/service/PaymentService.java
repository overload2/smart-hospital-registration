package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.PaymentDTO;
import com.hospital.registration.dto.PaymentQueryDTO;
import com.hospital.registration.vo.PaymentVO;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

/**
 * @title: PaymentService
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 支付服务接口
 */
@Service
public interface PaymentService {

    /**
     * 创建支付订单
     * @param paymentDTO 支付信息
     * @return 支付订单信息
     */
    PaymentVO createPayment(PaymentDTO paymentDTO);

    /**
     * 处理支付回调（模拟）
     * @param transactionNo 交易流水号
     * @return 是否成功
     */
    boolean handlePaymentCallback(String transactionNo);

    /**
     * 根据挂号ID查询支付记录
     * @param registrationId 挂号ID
     * @return 支付记录
     */
    PaymentVO getPaymentByRegistrationId(Long registrationId);

    /**
     * 根据交易流水号查询支付记录
     * @param transactionNo 交易流水号
     * @return 支付记录
     */
    PaymentVO getPaymentByTransactionNo(String transactionNo);

    /**
     * 查询用户的支付记录
     * @param userId 用户ID
     * @return 支付记录列表
     */
    List<PaymentVO> getUserPayments(Long userId);

    /**
     * 异步退款
     * @param registrationNo 挂号单号
     * @param amount 退款金额
     */
    void refundAsync(String registrationNo, BigDecimal amount);

    /**
     * 分页查询支付记录
     */
    Page<PaymentVO> getPaymentPage(PaymentQueryDTO queryDTO);

    /**
     * 手动退款
     */
    void manualRefund(Long id, String remark);
}
