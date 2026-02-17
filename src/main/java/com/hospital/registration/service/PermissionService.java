package com.hospital.registration.service;

import com.hospital.registration.dto.AssignRoleDTO;
import com.hospital.registration.dto.PermissionDTO;
import com.hospital.registration.vo.PermissionVO;
import com.hospital.registration.vo.UserRoleVO;

import java.util.List;

/**
 * @title: PermissionService
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限服务接口
 */
public interface PermissionService {

    /**
     * 查询权限树
     *
     * @return 权限VO树形列表
     */
    List<PermissionVO> getPermissionTree();

    /**
     * 查询所有权限列表（平铺）
     *
     * @return 权限VO列表
     */
    List<PermissionVO> getAllPermissions();

    /**
     * 根据ID查询权限详情
     *
     * @param id 权限ID
     * @return 权限VO
     */
    PermissionVO getPermissionById(Long id);

    /**
     * 创建权限
     *
     * @param permissionDTO 权限DTO
     * @return 权限VO
     */
    PermissionVO createPermission(PermissionDTO permissionDTO);

    /**
     * 修改权限
     *
     * @param id            权限ID
     * @param permissionDTO 权限DTO
     * @return 权限VO
     */
    PermissionVO updatePermission(Long id, PermissionDTO permissionDTO);

    /**
     * 删除权限
     *
     * @param id 权限ID
     */
    void deletePermission(Long id);

    /**
     * 为用户分配角色
     *
     * @param assignRoleDTO 分配角色DTO
     */
    void assignRolesToUser(AssignRoleDTO assignRoleDTO);

    /**
     * 查询用户的角色和权限
     *
     * @param userId 用户ID
     * @return 用户角色VO
     */
    UserRoleVO getUserRoles(Long userId);

    /**
     * 查询用户的权限编码列表
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    List<String> getUserPermissionCodes(Long userId);

    /**
     * 检查用户是否拥有指定权限
     *
     * @param userId         用户ID
     * @param permissionCode 权限编码
     * @return 是否拥有权限
     */
    boolean hasPermission(Long userId, String permissionCode);
}
