package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MedicalRecord
 * @author: Su
 * @date: 2026/1/11 19:53
 * @version: 1.0
 * @description: 就诊记录表
 */
@Data
@TableName("medical_record")
public class MedicalRecord {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 挂号ID
    @TableField("registration_id")
    private Long registrationId;

    // 患者ID
    @TableField("patient_id")
    private Long patientId;

    // 医生ID
    @TableField("doctor_id")
    private Long doctorId;

    // 主诉（新增）
    @TableField("chief_complaint")
    private String chiefComplaint;

    // 现病史（新增）
    @TableField("present_illness")
    private String presentIllness;

    // 诊断结果
    private String diagnosis;

    // 处方
    private String prescription;

    // 医嘱
    private String advice;

    // 就诊时间
    @TableField("visit_time")
    private LocalDateTime visitTime;

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

    // 既往史
    @TableField("past_history")
    private String pastHistory;

    // 过敏史
    @TableField("allergy_history")
    private String allergyHistory;
}
