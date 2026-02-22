package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title: RoleDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色DTO
 */
@Data
public class RoleDTO {

    // 角色编码
    private String roleCode;

    // 角色名称
    private String roleName;

    // 角色描述
    private String description;

    // 状态：0-禁用，1-启用
    private Integer status;
}
