package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import lombok.Getter;

/**
 * @title: MessageType
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息类型枚举
 */
@Getter
public enum MessageType {

    REGISTRATION_SUCCESS("REGISTRATION_SUCCESS", "挂号成功"),
    PAYMENT_SUCCESS("PAYMENT_SUCCESS", "支付成功"),
    REGISTRATION_CANCEL("REGISTRATION_CANCEL", "挂号取消"),
    REFUND_SUCCESS("REFUND_SUCCESS", "退款成功"),
    VISIT_REMINDER("VISIT_REMINDER", "就诊提醒"),
    QUEUE_CALL("QUEUE_CALL", "排队叫号"),
    SYSTEM_ANNOUNCEMENT("SYSTEM_ANNOUNCEMENT", "系统公告");

    @EnumValue
    private final String code;
    private final String name;

    MessageType(String code, String name) {
        this.code = code;
        this.name = name;
    }
}