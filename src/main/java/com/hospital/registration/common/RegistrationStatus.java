package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
    PENDING(1, "PENDING", "待就诊"),
    CALLED(2, "CALLED", "已叫号"),
    CONSULTING(3, "CONSULTING", "就诊中"),
    COMPLETED(4, "COMPLETED", "已完成"),
    CANCELLED(5, "CANCELLED", "已取消"),
    MISSED(6, "MISSED", "已过号");

    private final Integer code;

    @EnumValue
    @JsonValue
    private final String dbValue;

    private final String name;

    RegistrationStatus(Integer code, String dbValue, String name) {
        this.code = code;
        this.dbValue = dbValue;
        this.name = name;
    }
}
