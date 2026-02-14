package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hospital.registration.common.TimeSlot;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @title: Schedule
 * @author: Su
 * @date: 2026/1/11 19:15
 * @version: 1.0
 * @description: 排班表
 */
@Data
@TableName("schedule")
public class Schedule {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 医生ID
    @TableField("doctor_id")
    private Long doctorId;

    // 科室ID
    @TableField("department_id")
    private Long departmentId;

    // 排班日期
    @TableField("schedule_date")
    private LocalDate scheduleDate;

    // 时间段：上午/下午/晚上
    @TableField("time_slot")
    private TimeSlot timeSlot;

    // 总号源数
    @TableField("total_number")
    private Integer totalNumber;

    // 剩余号源数
    @TableField("remaining_number")
    private Integer remainingNumber;

    // 状态：0-已取消，1-可预约，2-已满
    private Integer status = 1;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 逻辑删除标识：0-未删除，1-已删除
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;
}
