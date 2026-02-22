package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 分页查询角色列表
     * @param current 当前页
     * @param size 每页大小
     * @param roleName 角色名称（可选）
     * @param roleCode 角色编码（可选）
     * @param status 状态（可选）
     * @return 分页结果
     */
    Page<RoleVO> getRolePage(Integer current, Integer size, String roleName, String roleCode, Integer status);

    /**
     * 批量更新角色状态
     * @param ids 角色ID列表
     * @param status 状态
     */
    void batchUpdateStatus(List<Long> ids, Integer status);
}
