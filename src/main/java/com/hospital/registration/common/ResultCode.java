package com.hospital.registration.common;

import lombok.Getter;

/**
 * @title: ResultCode
 * @author: Su
 * @date: 2026/1/7 19:52
 * @version: 1.0
 * @description: TODO 请添加描述
 */
@Getter
public enum ResultCode {
    // 通用状态码
    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),

    // 参数相关 4xx
    PARAM_ERROR(400, "参数错误"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),

    // 用户相关 1xxx
    USER_NOT_EXIST(1001, "用户不存在"),
    USER_ALREADY_EXIST(1002, "用户已存在"),
    PASSWORD_ERROR(1003, "密码错误"),

    // 挂号相关 2xxx
    REGISTRATION_FULL(2001, "号源已满"),
    REGISTRATION_NOT_FOUND(2002, "挂号记录不存在"),
    REGISTRATION_CANCELLED(2003, "挂号已取消"),

    // 医生相关 3xxx
    DOCTOR_NOT_FOUND(3001, "医生不存在"),
    SCHEDULE_NOT_FOUND(3002, "排班不存在");

    private final Integer code;
    private final String message;

    // 构造方法
    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

}
