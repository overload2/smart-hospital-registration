package com.hospital.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: Doctor
 * @author: Su
 * @date: 2026/1/11 19:11
 * @version: 1.0
 * @description: 医生表
 */
@Data
@Entity
@Table(name = "doctor")
public class Doctor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 关联用户ID
    @Column(name = "user_id", nullable = false)
    private Long userId;

    // 关联科室ID
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    // 职称：主任医师/副主任医师/主治医师/住院医师
    @Column(length = 50)
    private String title;

    // 专长
    @Column(length = 500)
    private String specialty;

    // 医生简介
    @Column(length = 1000)
    private String introduction;

    // 挂号费
    @Column(name = "registration_fee", precision = 10, scale = 2)
    private BigDecimal registrationFee;

    // 评分（0-5分）
    @Column(precision = 3, scale = 2)
    private BigDecimal rating = BigDecimal.ZERO;

    // 接诊数
    @Column(name = "consultation_count")
    private Integer consultationCount = 0;

    // 状态：0-禁用，1-启用
    @Column(nullable = false)
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}
