package com.hospital.registration.dto;

/**
 * @title: DoctorDTO
 * @author: Su
 * @date: 2026/1/18 23:31
 * @version: 1.0
 * @description: TODO 请添加描述
 */

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.math.BigDecimal;

/**
 * @title: DoctorDTO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 医生DTO - 用于创建和更新医生信息
 */
@Data
public class DoctorDTO {

    // 关联用户ID（必填）
    @NotNull(message = "用户ID不能为空")
    private Long userId;

    // 关联科室ID（必填）
    @NotNull(message = "科室ID不能为空")
    private Long departmentId;

    // 职称（必填）
    @NotBlank(message = "职称不能为空")
    @Size(max = 50, message = "职称长度不能超过50字符")
    private String title;

    // 专长（可选，最多200字）
    @Size(max = 200, message = "专长不能超过200字")
    private String specialty;

    // 医生简介（可选，最多1000字）
    @Size(max = 1000, message = "医生简介不能超过1000字")
    private String introduction;

    // 挂号费（必填，必须大于0）
    @NotNull(message = "挂号费不能为空")
    @DecimalMin(value = "0.01", message = "挂号费必须大于0")
    @DecimalMax(value = "9999.99", message = "挂号费不能超过9999.99")
    private BigDecimal registrationFee;
}
