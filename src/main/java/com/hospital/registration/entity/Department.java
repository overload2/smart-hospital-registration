package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: Department
 * @author: Su
 * @date: 2026/1/11 19:10
 * @version: 1.0
 * @description: 科室表
 */
@Data
@TableName("department")
public class Department {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 科室名称
    private String name;

    // 科室编码（唯一）
    private String code;

    // 科室简介
    private String description;

    // 科室位置
    private String location;

    // 科室电话
    private String phone;

    // 状态：0-禁用，1-启用
    private Integer status = 1;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
