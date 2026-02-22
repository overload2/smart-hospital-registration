package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title: AnnouncementDTO
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告DTO
 */
@Data
public class AnnouncementDTO {

    /**
     * 公告标题
     */
    @NotBlank(message = "公告标题不能为空")
    private String title;

    /**
     * 公告内容
     */
    private String content;

    /**
     * 状态：0-下架，1-发布
     */
    private Integer status;
}
