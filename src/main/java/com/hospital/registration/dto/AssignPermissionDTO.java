package com.hospital.registration.dto;

import lombok.Data;

import java.util.List;

/**
 * @title: AssignPermissionDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色分配权限DTO
 */
@Data
public class AssignPermissionDTO {

    // 角色ID
    private Long roleId;

    // 权限ID列表
    private List<Long> permissionIds;
}
