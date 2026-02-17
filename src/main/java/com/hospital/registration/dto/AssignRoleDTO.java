package com.hospital.registration.dto;

import lombok.Data;

import java.util.List;

/**
 * @title: AssignRoleDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 用户分配角色DTO
 */
@Data
public class AssignRoleDTO {

    // 用户ID
    private Long userId;

    // 角色ID列表
    private List<Long> roleIds;
}
