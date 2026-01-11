package com.hospital.registration.common;

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

    MORNING(1, "上午", "08:00-12:00"),
    AFTERNOON(2, "下午", "14:00-17:00"),
    EVENING(3, "晚上", "18:00-21:00");

    private final Integer code;
    private final String name;
    private final String timeRange;

    TimeSlot(Integer code, String name, String timeRange) {
        this.code = code;
        this.name = name;
        this.timeRange = timeRange;
    }
}
