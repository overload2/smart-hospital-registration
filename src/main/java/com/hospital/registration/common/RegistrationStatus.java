package com.hospital.registration.common;

import lombok.Getter;

/**
 * @title: RegistrationStatus
 * @author: Su
 * @date: 2026/1/11 17:19
 * @version: 1.0
 * @description: 挂号状态枚举
 */
@Getter
public enum RegistrationStatus {

    PENDING(1, "待支付"),
    CONFIRMED(2, "已确认"),
    COMPLETED(3, "已完成"),
    CANCELLED(4, "已取消");

    private final Integer code;
    private final String name;

    RegistrationStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
