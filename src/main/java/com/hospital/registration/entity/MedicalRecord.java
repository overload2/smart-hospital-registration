package com.hospital.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @title: MedicalRecord
 * @author: Su
 * @date: 2026/1/11 19:53
 * @version: 1.0
 * @description: 就诊记录表
 */
@Data
@Entity
@Table(name = "medical_record")
public class MedicalRecord {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 挂号ID
    @Column(name = "registration_id", nullable = false)
    private Long registrationId;

    // 患者ID
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    // 医生ID
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    // 诊断结果
    @Column(length = 1000)
    private String diagnosis;

    // 处方
    @Column(length = 2000)
    private String prescription;

    // 医嘱
    @Column(length = 1000)
    private String advice;

    // 就诊时间
    @Column(name = "visit_time")
    private LocalDateTime visitTime;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

