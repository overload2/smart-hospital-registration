package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
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
    MALE(1, "MALE", "男"),
    FEMALE(2, "FEMALE", "女");

    private final Integer code;

    @EnumValue  // MyBatis-Plus 使用此字段值存入数据库
    @JsonValue  // JSON 序列化时使用此字段值
    private final String dbValue;

    private final String name;

    Gender(Integer code, String dbValue, String name) {
        this.code = code;
        this.dbValue = dbValue;
        this.name = name;
    }
}
