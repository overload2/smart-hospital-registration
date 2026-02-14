package com.hospital.registration.vo;

import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.TimeSlot;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @title: RegistrationVO
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 挂号VO - 返回给前端的挂号信息
 */
@Data
public class RegistrationVO {

    // 挂号ID
    private Long id;

    // 挂号单号
    private String registrationNo;

    // 患者ID
    private Long patientId;

    // 患者姓名
    private String patientName;

    // 患者手机号
    private String patientPhone;

    // 医生ID
    private Long doctorId;

    // 医生姓名
    private String doctorName;

    // 医生职称
    private String doctorTitle;

    // 科室ID
    private Long departmentId;

    // 科室名称
    private String departmentName;

    // 排班ID
    private Long scheduleId;

    // 挂号日期（就诊日期）
    private LocalDate registrationDate;

    // 就诊时间段
    private TimeSlot timeSlot;

    // 时间段名称
    private String timeSlotName;

    // 时间范围
    private String timeRange;

    // 排队号
    private Integer queueNumber;

    // 挂号费
    private BigDecimal registrationFee;

    // 挂号状态
    private RegistrationStatus status;

    // 挂号状态名称
    private String statusName;

    // 症状描述
    private String symptom;

    // 支付状态
    private PaymentStatus paymentStatus;

    // 支付状态名称
    private String paymentStatusName;

    // 支付时间
    private LocalDateTime paymentTime;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}

