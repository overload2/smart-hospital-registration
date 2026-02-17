package com.hospital.registration.vo;

import lombok.Data;

import java.util.List;

/**
 * @title: UserRoleVO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 用户角色VO
 */
@Data
public class UserRoleVO {

    // 用户ID
    private Long userId;

    // 用户名
    private String username;

    // 真实姓名
    private String realName;

    // 角色列表
    private List<RoleVO> roles;

    // 权限编码列表（所有角色权限的并集）
    private List<String> permissions;
}
