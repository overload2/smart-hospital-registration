package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 系统配置实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置信息
 */
@Data
@TableName("sys_config")
public class SysConfig {

    // 配置ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 配置键
    private String configKey;

    // 配置值
    private String configValue;

    // 配置名称
    private String configName;

    // 配置类型(STRING/NUMBER/BOOLEAN/JSON)
    private String configType;

    // 备注
    private String remark;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
