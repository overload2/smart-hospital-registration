package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.TimeSlot;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @title: Registration
 * @author: Su
 * @date: 2026/1/11 19:25
 * @version: 1.0
 * @description: 挂号记录表
 */
@Data
@TableName("registration")
public class Registration {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 挂号单号（唯一）
    @TableField("registration_no")
    private String registrationNo;

    // 患者ID
    @TableField("patient_id")
    private Long patientId;

    // 医生ID
    @TableField("doctor_id")
    private Long doctorId;

    // 科室ID
    @TableField("department_id")
    private Long departmentId;

    // 排班ID
    @TableField("schedule_id")
    private Long scheduleId;

    // 挂号日期（就诊日期）
    @TableField("registration_date")
    private LocalDate registrationDate;

    // 就诊时间段
    @TableField("time_slot")
    private TimeSlot timeSlot;

    // 排队号
    @TableField("queue_number")
    private Integer queueNumber;

    // 挂号费
    @TableField("registration_fee")
    private BigDecimal registrationFee;

    // 挂号状态
    private RegistrationStatus status;

    // 症状描述
    private String symptom;

    // 支付状态
    @TableField("payment_status")
    private PaymentStatus paymentStatus;

    // 支付时间
    @TableField("payment_time")
    private LocalDateTime paymentTime;

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
