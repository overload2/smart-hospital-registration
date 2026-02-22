package com.hospital.registration.vo;

import com.hospital.registration.common.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: PaymentVO
 * @author: Su
 * @date: 2026/2/16
 * @version: 1.0
 * @description: 支付视图对象
 */
@Data
public class PaymentVO {

    /**
     * 支付ID
     */
    private Long id;

    /**
     * 交易流水号
     */
    private String transactionNo;

    /**
     * 挂号ID
     */
    private Long registrationId;

    /**
     * 挂号单号
     */
    private String registrationNo;

    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 患者姓名
     */
    private String patientName;

    // 患者手机号
    private String patientPhone;

    /**
     * 支付金额
     */
    private BigDecimal amount;

    /**
     * 支付方式
     */
    private String paymentMethod;

    /**
     * 支付方式名称
     */
    private String paymentMethodName;

    /**
     * 支付状态
     */
    private PaymentStatus paymentStatus;

    /**
     * 支付状态名称
     */
    private String paymentStatusName;

    /**
     * 支付时间
     */
    private LocalDateTime paymentTime;

    /**
     * 退款时间
     */
    private LocalDateTime refundTime;

    /**
     * 备注
     */
    private String remark;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}

