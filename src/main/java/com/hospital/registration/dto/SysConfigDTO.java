package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title 系统配置DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置新增/修改请求参数
 */
@Data
public class SysConfigDTO {

    // 配置ID（修改时必填）
    private Long id;

    // 配置键
    @NotBlank(message = "配置键不能为空")
    private String configKey;

    // 配置值
    @NotBlank(message = "配置值不能为空")
    private String configValue;

    // 配置名称
    @NotBlank(message = "配置名称不能为空")
    private String configName;

    // 配置类型
    private String configType;

    // 备注
    private String remark;
}
