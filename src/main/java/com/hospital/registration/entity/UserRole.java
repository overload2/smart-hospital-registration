package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: UserRole
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 用户角色关联表
 */
@Data
@TableName("user_role")
public class UserRole {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 角色ID
    @TableField("role_id")
    private Long roleId;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}

