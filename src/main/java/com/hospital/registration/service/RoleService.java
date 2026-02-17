package com.hospital.registration.service;

import com.hospital.registration.dto.AssignPermissionDTO;
import com.hospital.registration.dto.RoleDTO;
import com.hospital.registration.vo.RoleVO;

import java.util.List;

/**
 * @title: RoleService
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色服务接口
 */
public interface RoleService {

    /**
     * 查询所有角色列表
     *
     * @return 角色VO列表
     */
    List<RoleVO> getAllRoles();

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色VO
     */
    RoleVO getRoleById(Long id);

    /**
     * 创建角色
     *
     * @param roleDTO 角色DTO
     * @return 角色VO
     */
    RoleVO createRole(RoleDTO roleDTO);

    /**
     * 修改角色
     *
     * @param id      角色ID
     * @param roleDTO 角色DTO
     * @return 角色VO
     */
    RoleVO updateRole(Long id, RoleDTO roleDTO);

    /**
     * 删除角色
     *
     * @param id 角色ID
     */
    void deleteRole(Long id);

    /**
     * 为角色分配权限
     *
     * @param assignPermissionDTO 分配权限DTO
     */
    void assignPermissions(AssignPermissionDTO assignPermissionDTO);

    /**
     * 查询角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    List<Long> getRolePermissionIds(Long roleId);
}
