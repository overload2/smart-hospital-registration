package com.hospital.registration.common;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @title: TimeSlot
 * @author: Su
 * @date: 2026/1/11
 * @version: 1.0
 * @description: 时间段枚举
 */
@Getter
public enum TimeSlot {

    // 粗粒度（兼容旧数据）
    MORNING("MORNING", "上午", "08:00-12:00", 1),
    AFTERNOON("AFTERNOON", "下午", "14:00-18:00", 3),
    EVENING("EVENING", "晚间", "18:00-20:00", 5),

    // 细粒度
    MORNING_EARLY("MORNING_EARLY", "上午早班", "08:00-10:00", 1),
    MORNING_LATE("MORNING_LATE", "上午晚班", "10:00-12:00", 2),
    AFTERNOON_EARLY("AFTERNOON_EARLY", "下午早班", "14:00-16:00", 3),
    AFTERNOON_LATE("AFTERNOON_LATE", "下午晚班", "16:00-18:00", 4);

    @EnumValue
    @JsonValue
    private final String dbValue;

    private final String name;

    private final String timeRange;

    private final Integer sortOrder;

    TimeSlot(String dbValue, String name, String timeRange, Integer sortOrder) {
        this.dbValue = dbValue;
        this.name = name;
        this.timeRange = timeRange;
        this.sortOrder = sortOrder;
    }
}
