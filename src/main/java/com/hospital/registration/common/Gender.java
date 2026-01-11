package com.hospital.registration.common;

import lombok.Getter;

/**
 * @title: Gender
 * @author: Su
 * @date: 2026/1/11 17:17
 * @version: 1.0
 * @description: 性别枚举
 */
@Getter
public enum Gender {
    MALE(1, "男"),
    FEMALE(2, "女"),
    UNKNOWN(0, "未知");

    private final Integer code;
    private final String name;

    Gender(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
