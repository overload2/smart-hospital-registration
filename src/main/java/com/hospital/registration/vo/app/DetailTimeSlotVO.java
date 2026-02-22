package com.hospital.registration.vo.app;

import lombok.Data;

/**
 * @title: DetailTimeSlotVO
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 细分时段VO
 */
@Data
public class DetailTimeSlotVO {

    // 时段编码，如 M_0800
    private String slotCode;

    // 时间范围，如 08:00-08:30
    private String timeRange;

    // 时段分类：MORNING/AFTERNOON/EVENING
    private String period;

    // 该时段总号源
    private Integer capacity;

    // 该时段已预约数
    private Integer bookedCount;

    // 该时段剩余号源
    private Integer remainingCount;

    // 是否可预约
    private Boolean available;

    // 不可预约原因：FULL-已满，BOOKED-已预约该时段
    private String unavailableReason;
}
