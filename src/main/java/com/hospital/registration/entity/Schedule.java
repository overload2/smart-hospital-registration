package com.hospital.registration.entity;

import com.hospital.registration.common.TimeSlot;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // 医生ID
    @Column(name = "doctor_id", nullable = false)
    private Long doctorId;

    // 科室ID
    @Column(name = "department_id", nullable = false)
    private Long departmentId;

    // 排班日期
    @Column(name = "schedule_date", nullable = false)
    private LocalDate scheduleDate;

    // 时间段：上午/下午/晚上
    @Enumerated(EnumType.STRING)
    @Column(name = "time_slot", nullable = false, length = 20)
    private TimeSlot timeSlot;

    // 总号源数
    @Column(name = "total_number", nullable = false)
    private Integer totalNumber;

    // 剩余号源数
    @Column(name = "remaining_number", nullable = false)
    private Integer remainingNumber;

    // 状态：0-已取消，1-可预约，2-已满
    @Column(nullable = false)
    private Integer status = 1;

    @CreationTimestamp
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    @UpdateTimestamp
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}

