package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: Role
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色表
 */
@Data
@TableName("role")
public class Role {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 角色编码
    @TableField("role_code")
    private String roleCode;

    // 角色名称
    @TableField("role_name")
    private String roleName;

    // 角色描述
    private String description;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 逻辑删除标识：0-未删除，1-已删除
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;
}
