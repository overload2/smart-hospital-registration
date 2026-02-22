package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @title: MessageChannel
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息渠道枚举
 */
@Getter
public enum MessageChannel {

    SMS("SMS", "短信"),
    WECHAT("WECHAT", "微信"),
    SYSTEM("SYSTEM", "站内消息");

    @EnumValue
    private final String code;
    private final String name;

    MessageChannel(String code, String name) {
        this.code = code;
        this.name = name;
    }
}

