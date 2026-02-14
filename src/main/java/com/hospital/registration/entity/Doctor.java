package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

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
@TableName("doctor")
public class Doctor {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 关联用户ID
    @TableField("user_id")
    private Long userId;

    // 关联科室ID
    @TableField("department_id")
    private Long departmentId;

    // 职称：主任医师/副主任医师/主治医师/住院医师
    private String title;

    // 专长
    private String specialty;

    // 医生简介
    private String introduction;

    // 挂号费
    @TableField("registration_fee")
    private BigDecimal registrationFee;

    // 评分（0-5分）
    private BigDecimal rating = BigDecimal.ZERO;

    // 接诊数
    @TableField("consultation_count")
    private Integer consultationCount = 0;

    // 状态：0-禁用，1-启用
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
