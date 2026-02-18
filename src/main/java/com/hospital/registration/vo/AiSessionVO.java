package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title AI会话VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI会话返回信息
 */
@Data
public class AiSessionVO {

    // 会话ID
    private Long id;

    // 用户ID
    private Long userId;

    // 会话类型
    private String sessionType;

    // 会话类型名称
    private String sessionTypeName;

    // 会话标题
    private String title;

    // 状态
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 消息列表
    private List<AiMessageVO> messages;
}
