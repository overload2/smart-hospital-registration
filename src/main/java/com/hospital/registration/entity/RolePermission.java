package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: RolePermission
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色权限关联表
 */
@Data
@TableName("role_permission")
public class RolePermission {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 角色ID
    @TableField("role_id")
    private Long roleId;

    // 权限ID
    @TableField("permission_id")
    private Long permissionId;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;
}
