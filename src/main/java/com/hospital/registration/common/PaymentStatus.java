package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
    PENDING(1, "PENDING", "待支付"),
    PAID(2, "PAID", "已支付"),
    REFUNDED(3, "REFUNDED", "已退款");

    private final Integer code;

    @EnumValue
    @JsonValue
    private final String dbValue;

    private final String name;

    PaymentStatus(Integer code, String dbValue, String name) {
        this.code = code;
        this.dbValue = dbValue;
        this.name = name;
    }
}
