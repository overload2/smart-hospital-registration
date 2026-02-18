package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title AI对话请求DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI对话请求参数
 */
@Data
public class AiChatDTO {

    // 会话ID（新会话不传）
    private Long sessionId;

    // 会话类型(GUIDE-智能导诊/CONSULT-健康咨询)，新会话必填
    private String sessionType;

    // 用户消息
    @NotBlank(message = "消息内容不能为空")
    private String message;
}
