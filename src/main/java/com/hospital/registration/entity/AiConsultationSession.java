package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title AI问诊会话实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊会话信息
 */
@Data
@TableName("ai_consultation_session")
public class AiConsultationSession {

    // 会话ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户ID
    private Long userId;

    // 会话类型(GUIDE-智能导诊/CONSULT-健康咨询)
    private String sessionType;

    // 会话标题
    private String title;

    // 状态(0已结束 1进行中)
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
