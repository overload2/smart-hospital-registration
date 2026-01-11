package com.hospital.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @title: DoctorEvaluation
 * @author: Su
 * @date: 2026/1/11 19:58
 * @version: 1.0
 * @description: 医生评价表
 */
@Data
@Entity
@Table(name = "doctor_evaluation")
public class DoctorEvaluation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 医生ID
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    // 患者ID
    @Column(name = "patient_id", nullable = false)
    private Long patientId;

    // 挂号ID
    @Column(name = "registration_id", nullable = false)
    private Long registrationId;

    // 评分（1-5分）
    @Column(nullable = false)
    private Integer rating;

    // 评价内容
    @Column(length = 1000)
    private String comment;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

