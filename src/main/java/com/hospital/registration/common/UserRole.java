package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @title: UserRole
 * @author: Su
 * @date: 2026/1/11 17:17
 * @version: 1.0
 * @description: 用户角色枚举
 */
@Getter
public enum UserRole {
    PATIENT(1, "PATIENT", "患者"),
    DOCTOR(2, "DOCTOR", "医生"),
    ADMIN(3, "ADMIN", "管理员");

    private final Integer code;

    @EnumValue
    @JsonValue
    private final String dbValue;

    private final String name;

    UserRole(Integer code, String dbValue, String name) {
        this.code = code;
        this.dbValue = dbValue;
        this.name = name;
    }
}
