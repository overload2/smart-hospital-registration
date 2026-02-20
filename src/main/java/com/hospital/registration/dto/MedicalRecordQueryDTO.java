package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title MedicalRecordQueryDTO
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 病历查询条件DTO
 */
@Data
public class MedicalRecordQueryDTO {

    // 患者姓名
    private String patientName;

    // 诊断结果（模糊查询）
    private String diagnosis;

    // 开始日期
    private String startDate;

    // 结束日期
    private String endDate;

    // 页码
    private Integer pageNum = 1;

    // 每页数量
    private Integer pageSize = 10;
}