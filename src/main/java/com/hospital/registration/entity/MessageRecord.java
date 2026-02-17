package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MessageRecord
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息记录表
 */
@Data
@TableName("message_record")
public class MessageRecord {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 接收用户ID
    @TableField("user_id")
    private Long userId;

    // 消息类型
    @TableField("message_type")
    private String messageType;

    // 发送渠道：SMS-短信，WECHAT-微信，SYSTEM-站内
    private String channel;

    // 消息标题
    private String title;

    // 消息内容
    private String content;

    // 接收地址（手机号/openId）
    private String receiver;

    // 发送状态：0-待发送，1-已发送，2-发送失败
    @TableField("send_status")
    private Integer sendStatus;

    // 发送时间
    @TableField("send_time")
    private LocalDateTime sendTime;

    // 阅读状态：0-未读，1-已读
    @TableField("read_status")
    private Integer readStatus;

    // 阅读时间
    @TableField("read_time")
    private LocalDateTime readTime;

    // 错误信息
    @TableField("error_msg")
    private String errorMsg;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

