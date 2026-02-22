package com.hospital.registration.dto.app;

import lombok.Data;
import jakarta.validation.constraints.NotNull;

/**
 * @title: AppRegistrationDTO
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端挂号请求DTO
 */
@Data
public class AppRegistrationDTO {

    @NotNull(message = "科室ID不能为空")
    private Long departmentId;

    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    @NotNull(message = "排班ID不能为空")
    private Long scheduleId;

    /** 就诊人ID（为他人挂号时使用，为空则为自己挂号） */
    private Long patientId;
}
