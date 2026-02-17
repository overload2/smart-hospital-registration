package com.hospital.registration.mq;


import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.config.RabbitMQConfig;
import com.hospital.registration.dto.RefundMessage;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.mapper.PaymentMapper;
import com.hospital.registration.service.MessageService;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.LocalDateTime;

/**
 * @title: RefundMessageConsumer
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 退款消息消费者
 */
@Slf4j
@Component
public class RefundMessageConsumer {

    private final PaymentMapper paymentMapper;
    private final MessageService messageService;

    public RefundMessageConsumer(PaymentMapper paymentMapper, MessageService messageService) {
        this.paymentMapper = paymentMapper;
        this.messageService = messageService;
    }

    /**
     * 监听退款队列，处理退款消息
     */
    @RabbitListener(queues = RabbitMQConfig.REFUND_QUEUE)
    public void handleRefundMessage(RefundMessage refundMessage, Message message, Channel channel) throws IOException {
        long deliveryTag = message.getMessageProperties().getDeliveryTag();

        try {
            log.info("【退款消费者】收到退款消息 - 挂号单号: {}, 金额: {}",
                    refundMessage.getRegistrationNo(), refundMessage.getAmount());

            // 模拟调用第三方支付接口进行退款
            processRefund(refundMessage);

            // 更新支付记录状态为已退款
            Payment payment = paymentMapper.selectByRegistrationId(refundMessage.getRegistrationId());
            if (payment != null) {
                Payment updatePayment = new Payment();
                updatePayment.setId(payment.getId());
                updatePayment.setPaymentStatus(PaymentStatus.REFUNDED);
                updatePayment.setRefundTime(LocalDateTime.now());
                updatePayment.setRemark("取消挂号退款");
                paymentMapper.updateById(updatePayment);
                //发送退款成功通知
                payment=paymentMapper.selectById(payment.getId());
                messageService.sendRefundSuccessNotice(payment);
            }

            // 手动确认消息
            channel.basicAck(deliveryTag, false);

            log.info("【退款消费者】退款处理成功 - 挂号单号: {}", refundMessage.getRegistrationNo());

        } catch (Exception e) {
            log.error("【退款消费者】退款处理失败 - 挂号单号: {}, 错误: {}",
                    refundMessage.getRegistrationNo(), e.getMessage(), e);

            // 拒绝消息，重新入队
            channel.basicNack(deliveryTag, false, true);
        }
    }

    /**
     * 处理退款逻辑（模拟）
     */
    private void processRefund(RefundMessage refundMessage) {
        // 模拟退款处理耗时
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 模拟退款成功
        log.info("【模拟退款】退款成功 - 挂号单号: {}, 退款金额: {} 元",
                refundMessage.getRegistrationNo(), refundMessage.getAmount());

        // TODO: 实际项目中，这里应该：
        // 1. 调用微信/支付宝退款API
        // 2. 发送退款成功通知给患者
    }

}
