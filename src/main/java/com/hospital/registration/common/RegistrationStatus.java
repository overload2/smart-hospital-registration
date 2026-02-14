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
    CONSULTING(2, "CONSULTING", "就诊中"),
    COMPLETED(3, "COMPLETED", "已完成"),
    CANCELLED(4, "CANCELLED", "已取消");

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
