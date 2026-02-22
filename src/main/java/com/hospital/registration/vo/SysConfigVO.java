package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 系统配置VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置返回信息
 */
@Data
public class SysConfigVO {

    // 配置ID
    private Long id;

    // 配置键
    private String configKey;

    // 配置值
    private String configValue;

    // 配置名称
    private String configName;

    // 配置类型
    private String configType;

    // 备注
    private String remark;

    // 更新时间
    private LocalDateTime updateTime;
}