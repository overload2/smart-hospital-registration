package com.hospital.registration.entity;

import com.hospital.registration.common.PaymentStatus;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: Payment
 * @author: Su
 * @date: 2026/1/11 19:50
 * @version: 1.0
 * @description: 支付记录表
 */
@Data
@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 交易流水号（唯一）
    @Column(name = "transaction_no", nullable = false, unique = true, length = 50)
    private String transactionNo;

    // 挂号ID
    @Column(name = "registration_id", nullable = false)
    private Long registrationId;

    // 用户ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 支付金额
    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    // 支付方式：ALIPAY-支付宝，WECHAT-微信，CASH-现金
    @Column(name = "payment_method", length = 20)
    private String paymentMethod;

    // 支付状态
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", nullable = false, length = 20)
    private PaymentStatus paymentStatus;

    // 支付时间
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    // 退款时间
    @Column(name = "refund_time")
    private LocalDateTime refundTime;

    // 备注
    @Column(length = 500)
    private String remark;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

