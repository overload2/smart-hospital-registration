package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @title: TimeSlot
 * @author: Su
 * @date: 2026/1/11 17:18
 * @version: 1.0
 * @description: 就诊时间段枚举
 */
@Getter
public enum TimeSlot {
    MORNING(1, "MORNING", "上午", "08:00-12:00"),
    AFTERNOON(2, "AFTERNOON", "下午", "14:00-17:00"),
    EVENING(3, "EVENING", "晚上", "18:00-21:00");

    private final Integer code;

    @EnumValue
    @JsonValue
    private final String dbValue;

    private final String name;
    private final String timeRange;

    TimeSlot(Integer code, String dbValue, String name, String timeRange) {
        this.code = code;
        this.dbValue = dbValue;
        this.name = name;
        this.timeRange = timeRange;
    }
}
