package com.hospital.registration.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: DoctorVO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 医生VO - 返回给前端的医生信息
 */
@Data
public class DoctorVO {

    // 医生ID
    private Long id;

    // 关联用户ID
    private Long userId;

    // 用户真实姓名
    private String realName;

    // 关联科室ID
    private Long departmentId;

    // 科室名称
    private String departmentName;

    // 职称
    private String title;

    // 专长
    private String specialty;

    // 医生简介
    private String introduction;

    // 挂号费
    private BigDecimal registrationFee;

    // 评分（0-5分）
    private BigDecimal rating;

    // 接诊数
    private Integer consultationCount;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}

