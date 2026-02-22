package com.hospital.registration.vo;

import com.hospital.registration.common.TimeSlot;
import com.hospital.registration.vo.app.DetailTimeSlotVO;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @title: ScheduleVO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 排班VO - 返回给前端的排班信息
 */
@Data
public class ScheduleVO {

    // 排班ID
    private Long id;

    // 医生ID
    private Long doctorId;

    // 医生姓名
    private String doctorName;

    // 科室ID
    private Long departmentId;

    // 科室名称
    private String departmentName;

    // 排班日期
    private LocalDate scheduleDate;

    // 时间段
    private TimeSlot timeSlot;

    // 时间段名称
    private String timeSlotName;

    // 时间范围
    private String timeRange;

    // 总号源数
    private Integer totalNumber;

    // 剩余号源数
    private Integer remainingNumber;

    // 状态：0-已取消，1-可预约，2-已满
    private Integer status;

    // 挂号费
    private BigDecimal registrationFee;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;

    // 每个细分时段号源数
    private Integer slotCapacity;

    // 细分时段列表（患者端展示用）
    private List<DetailTimeSlotVO> detailSlots;
}

