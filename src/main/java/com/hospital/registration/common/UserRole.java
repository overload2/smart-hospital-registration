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
    USER(5, "USER", "普通用户"),
    PATIENT(4, "PATIENT", "患者"),
    DOCTOR(3, "DOCTOR", "医生"),
    ADMIN(2, "ADMIN", "管理员"),
    SUPER_ADMIN(1, "SUPER_ADMIN", "超级管理员");

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
