package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @title: SendStatus
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息发送状态枚举
 */
@Getter
public enum SendStatus {

    PENDING(0, "待发送"),
    SENT(1, "已发送"),
    FAILED(2, "发送失败");

    @EnumValue
    private final Integer code;
    private final String name;

    SendStatus(Integer code, String name) {
        this.code = code;
        this.name = name;
    }
}
