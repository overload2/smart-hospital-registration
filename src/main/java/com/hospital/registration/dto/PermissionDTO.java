package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title: PermissionDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限DTO
 */
@Data
public class PermissionDTO {

    // 父权限ID（0表示顶级）
    private Long parentId;

    // 权限编码
    private String permissionCode;

    // 权限名称
    private String permissionName;

    // 权限类型：MENU-菜单，BUTTON-按钮
    private String permissionType;

    // 路由路径
    private String path;

    // 图标
    private String icon;

    // 排序号
    private Integer sortOrder;

    // 状态：0-禁用，1-启用
    private Integer status;
}
