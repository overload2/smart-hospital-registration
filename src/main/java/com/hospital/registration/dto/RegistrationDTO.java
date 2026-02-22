package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @title: RegistrationDTO
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 挂号DTO - 用于创建挂号记录
 */
@Data
public class RegistrationDTO {

    // 排班ID（必填）
    @NotNull(message = "排班ID不能为空")
    private Long scheduleId;

    // 患者ID（患者端由后端自动设置）
    private Long patientId;

    // 症状描述（可选，最多500字）
    @Size(max = 500, message = "症状描述不能超过500字")
    private String symptom;

    // 细分时段编码（患者选择的具体时段）
    @NotBlank(message = "请选择就诊时段")
    private String detailTimeSlot;
}

