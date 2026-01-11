package com.hospital.registration.entity;

/**
 * @title: Registration
 * @author: Su
 * @date: 2026/1/11 19:21
 * @version: 1.0
 * @description: 挂号记录表
 */

import com.hospital.registration.common.PaymentStatus;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.TimeSlot;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity
@Table(name = "registration")
public class Registration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 挂号单号（唯一）
    @Column(name = "registration_no", nullable = false, unique = true, length = 50)
    private String registrationNo;

    // 患者ID
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    // 医生ID
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    // 科室ID
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    // 排班ID
    @Column(name = "schedule_id", nullable = false)
    private Long scheduleId;

    // 挂号日期（就诊日期）
    @Column(name = "registration_date", nullable = false)
    private LocalDate registrationDate;

    // 就诊时间段
    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot", nullable = false, length = 20)
    private TimeSlot timeSlot;

    // 排队号
    @Column(name = "queue_number")
    private Integer queueNumber;

    // 挂号费
    @Column(name = "registration_fee", precision = 10, scale = 2)
    private BigDecimal registrationFee;

    // 挂号状态
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RegistrationStatus status;

    // 症状描述
    @Column(length = 500)
    private String symptom;

    // 支付状态
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_status", length = 20)
    private PaymentStatus paymentStatus;

    // 支付时间
    @Column(name = "payment_time")
    private LocalDateTime paymentTime;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

