package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: MessageRecordVO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息记录VO
 */
@Data
public class MessageRecordVO {

    // 主键ID
    private Long id;

    // 接收用户ID
    private Long userId;

    // 消息类型
    private String messageType;

    // 消息类型名称
    private String messageTypeName;

    // 发送渠道
    private String channel;

    // 渠道名称
    private String channelName;

    // 消息标题
    private String title;

    // 消息内容
    private String content;

    // 发送状态：0-待发送，1-已发送，2-发送失败
    private Integer sendStatus;

    // 发送状态名称
    private String sendStatusName;

    // 发送时间
    private LocalDateTime sendTime;

    // 阅读状态：0-未读，1-已读
    private Integer readStatus;

    // 阅读时间
    private LocalDateTime readTime;

    // 创建时间
    private LocalDateTime createTime;

    // 用户姓名
    private String userName;

    // 用户手机号
    private String userPhone;
}

