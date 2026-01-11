package com.hospital.registration.common;

import lombok.Getter;

/**
 * @title: PaymentStatus
 * @author: Su
 * @date: 2026/1/11 17:19
 * @version: 1.0
 * @description: 支付状态枚举
 */
@Getter
public enum PaymentStatus {
    PENDING(1, "待支付"),
    SUCCESS(2, "支付成功"),
    FAILED(3, "支付失败"),
    REFUNDED(4, "已退款");

    private final Integer code;
    private final String name;

    PaymentStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
