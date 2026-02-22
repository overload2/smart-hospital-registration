package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MessageTemplateVO
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板VO
 */
@Data
public class MessageTemplateVO {

    private Long id;

    // 模板编码
    private String templateCode;

    // 模板名称
    private String templateName;

    // 消息类型
    private String messageType;

    // 消息类型名称
    private String messageTypeName;

    // 发送渠道
    private String channel;

    // 渠道名称
    private String channelName;

    // 标题模板
    private String titleTemplate;

    // 内容模板
    private String contentTemplate;

    // 变量说明
    private String variables;

    // 状态
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
