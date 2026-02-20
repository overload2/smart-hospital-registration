package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MessageTemplate
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板表
 */
@Data
@TableName("message_template")
public class MessageTemplate {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 模板编码
    @TableField("template_code")
    private String templateCode;

    // 模板名称
    @TableField("template_name")
    private String templateName;

    // 消息类型
    @TableField("message_type")
    private String messageType;

    // 发送渠道
    private String channel;

    // 标题模板
    @TableField("title_template")
    private String titleTemplate;

    // 内容模板
    @TableField("content_template")
    private String contentTemplate;

    // 变量说明（JSON格式）
    private String variables;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
