package com.hospital.registration.common;

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
    PATIENT(1, "患者"),
    DOCTOR(2, "医生"),
    ADMIN(3, "管理员");

    private final Integer code;
    private final String name;

    UserRole(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
