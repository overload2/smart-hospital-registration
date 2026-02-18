package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title AI问诊消息实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊消息记录
 */
@Data
@TableName("ai_consultation_message")
public class AiConsultationMessage {

    // 消息ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 会话ID
    private Long sessionId;

    // 角色(user-用户/assistant-AI)
    private String role;

    // 消息内容
    private String content;

    // 推荐科室ID
    private Long recommendedDepartmentId;

    // 推荐医生ID
    private Long recommendedDoctorId;

    // 创建时间
    private LocalDateTime createTime;
}
