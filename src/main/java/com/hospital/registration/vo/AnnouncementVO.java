package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: AnnouncementVO
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告VO
 */
@Data
public class AnnouncementVO {

    private Long id;

    /**
     * 公告标题
     */
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 状态：0-下架，1-发布
     */
    private Integer status;

    /**
     * 状态名称
     */
    private String statusName;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
