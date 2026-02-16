package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

/**
 * @title: MedicalRecordDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 病历请求DTO
 */
@Data
public class MedicalRecordDTO {

    /**
     * 挂号ID
     */
    @NotNull(message = "挂号ID不能为空")
    private Long registrationId;

    /**
     * 主诉
     */
    @NotBlank(message = "主诉不能为空")
    private String chiefComplaint;

    /**
     * 现病史
     */
    private String presentIllness;

    /**
     * 诊断结果
     */
    @NotBlank(message = "诊断结果不能为空")
    private String diagnosis;

    /**
     * 处方
     */
    private String prescription;

    /**
     * 医嘱
     */
    private String advice;
}

