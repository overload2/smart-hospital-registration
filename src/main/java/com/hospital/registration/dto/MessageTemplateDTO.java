package com.hospital.registration.dto;

import lombok.Data;
import jakarta.validation.constraints.NotBlank;

/**
 * @title: MessageTemplateDTO
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板DTO
 */
@Data
public class MessageTemplateDTO {

    // 模板编码
    @NotBlank(message = "模板编码不能为空")
    private String templateCode;

    // 模板名称
    @NotBlank(message = "模板名称不能为空")
    private String templateName;

    // 消息类型
    @NotBlank(message = "消息类型不能为空")
    private String messageType;

    // 发送渠道
    @NotBlank(message = "发送渠道不能为空")
    private String channel;

    // 标题模板
    private String titleTemplate;

    // 内容模板
    @NotBlank(message = "内容模板不能为空")
    private String contentTemplate;

    // 变量说明
    private String variables;

    // 状态
    private Integer status = 1;
}

