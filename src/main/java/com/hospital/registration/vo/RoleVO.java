package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title: RoleVO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色VO
 */
@Data
public class RoleVO {

    // 主键ID
    private Long id;

    // 角色编码
    private String roleCode;

    // 角色名称
    private String roleName;

    // 角色描述
    private String description;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 状态名称
    private String statusName;

    // 创建时间
    private LocalDateTime createTime;

    // 权限ID列表（用于角色授权回显）
    private List<Long> permissionIds;
}
