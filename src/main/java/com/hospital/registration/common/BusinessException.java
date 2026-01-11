package com.hospital.registration.common;

import lombok.Getter;

/**
 * @title: BusinessException
 * @author: Su
 * @date: 2026/1/11 17:07
 * @version: 1.0
 * @description: 自定义业务异常
 */
@Getter
public class BusinessException extends RuntimeException {
    private Integer code;

    // 构造方法1：只传消息（默认500）
    public BusinessException(String message) {
        super(message);
        this.code = 500;
    }

    // 构造方法2：传状态码和消息
    public BusinessException(Integer code, String message) {
        super(message);
        this.code = code;
    }

    // 构造方法3：传 ResultCode 枚举（推荐使用）
    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.code = resultCode.getCode();
    }
}
