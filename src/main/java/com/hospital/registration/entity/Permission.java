package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: Permission
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限表
 */
@Data
@TableName("permission")
public class Permission {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 父权限ID（0表示顶级）
    @TableField("parent_id")
    private Long parentId;

    // 权限编码
    @TableField("permission_code")
    private String permissionCode;

    // 权限名称
    @TableField("permission_name")
    private String permissionName;

    // 权限类型：MENU-菜单，BUTTON-按钮
    @TableField("permission_type")
    private String permissionType;

    // 路由路径
    private String path;

    // 图标
    private String icon;

    // 排序号
    @TableField("sort_order")
    private Integer sortOrder;

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
