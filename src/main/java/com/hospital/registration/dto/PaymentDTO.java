package com.hospital.registration.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: PaymentDTO
 * @author: Su
 * @date: 2026/2/16
 * @version: 1.0
 * @description: 支付请求DTO
 */
@Data
public class PaymentDTO {

    /**
     * 挂号ID
     */
    @NotNull(message = "挂号ID不能为空")
    private Long registrationId;

    /**
     * 支付方式：WECHAT-微信，ALIPAY-支付宝，CASH-现金
     */
    @NotNull(message = "支付方式不能为空")
    private String paymentMethod;

    /**
     * 支付金额
     */
    @NotNull(message = "支付金额不能为空")
    private BigDecimal amount;
}