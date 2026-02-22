package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MedicalRecordVO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 病历视图对象
 */
@Data
public class MedicalRecordVO {

    /**
     * 病历ID
     */
    private Long id;

    /**
     * 挂号ID
     */
    private Long registrationId;

    /**
     * 挂号单号
     */
    private String registrationNo;

    /**
     * 患者ID
     */
    private Long patientId;

    /**
     * 患者姓名
     */
    private String patientName;

    /**
     * 患者性别
     */
    private String patientGender;

    /**
     * 患者年龄
     */
    private Integer patientAge;

    /**
     * 医生ID
     */
    private Long doctorId;

    /**
     * 医生姓名
     */
    private String doctorName;

    /**
     * 科室名称
     */
    private String departmentName;

    /**
     * 主诉
     */
    private String chiefComplaint;

    /**
     * 现病史
     */
    private String presentIllness;

    /**
     * 诊断结果
     */
    private String diagnosis;

    /**
     * 处方
     */
    private String prescription;

    /**
     * 医嘱
     */
    private String advice;

    /**
     * 就诊时间
     */
    private LocalDateTime visitTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 既往史
     */
    private String pastHistory;

    /**
     * 过敏史
     */
    private String allergyHistory;
}
