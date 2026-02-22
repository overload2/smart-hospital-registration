package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title: PermissionVO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限VO（支持树形结构）
 */
@Data
public class PermissionVO {

    // 主键ID
    private Long id;

    // 父权限ID
    private Long parentId;

    // 权限编码
    private String permissionCode;

    // 权限名称
    private String permissionName;

    // 权限类型：MENU-菜单，BUTTON-按钮
    private String permissionType;

    // 权限类型名称
    private String permissionTypeName;

    // 路由路径
    private String path;

    // 图标
    private String icon;

    // 排序号
    private Integer sortOrder;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 状态名称
    private String statusName;

    // 创建时间
    private LocalDateTime createTime;

    // 子权限列表（树形结构）
    private List<PermissionVO> children;
}
