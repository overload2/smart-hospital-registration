package com.hospital.registration.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * @title: Department
 * @author: Su
 * @date: 2026/1/11 19:10
 * @version: 1.0
 * @description: 科室表
 */
@Data
@Entity
@Table(name = "department")
public class Department {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 科室名称
    @Column(nullable = false, length = 50)
    private String name;

    // 科室编码（唯一）
    @Column(nullable = false, unique = true, length = 20)
    private String code;

    // 科室简介
    @Column(length = 500)
    private String description;

    // 科室位置
    @Column(length = 100)
    private String location;

    // 科室电话
    @Column(length = 20)
    private String phone;

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
