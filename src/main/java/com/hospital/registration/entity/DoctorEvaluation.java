package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: DoctorEvaluation
 * @author: Su
 * @date: 2026/1/11 19:58
 * @version: 1.0
 * @description: 医生评价表
 */
@Data
@TableName("doctor_evaluation")
public class DoctorEvaluation {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 医生ID
    @TableField("doctor_id")
    private Long doctorId;

    // 患者ID
    @TableField("patient_id")
    private Long patientId;

    // 挂号ID
    @TableField("registration_id")
    private Long registrationId;

    // 评分（1-5分）
    private Integer rating;

    // 评价内容
    private String comment;

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
