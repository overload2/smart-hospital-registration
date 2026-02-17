package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.Permission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: PermissionMapper
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限Mapper
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {

    /**
     * 根据角色ID查询权限列表
     *
     * @param roleId 角色ID
     * @return 权限列表
     */
    List<Permission> selectByRoleId(@Param("roleId") Long roleId);

    /**
     * 根据用户ID查询权限列表（通过用户角色关联）
     *
     * @param userId 用户ID
     * @return 权限列表
     */
    List<Permission> selectByUserId(@Param("userId") Long userId);

    /**
     * 根据权限编码查询权限
     *
     * @param permissionCode 权限编码
     * @return 权限实体
     */
    Permission selectByPermissionCode(@Param("permissionCode") String permissionCode);

    /**
     * 查询所有权限列表
     *
     * @return 权限列表
     */
    List<Permission> selectAllPermissions();

    /**
     * 统计子权限数量
     *
     * @param parentId 父权限ID
     * @return 子权限数量
     */
    Integer countByParentId(@Param("parentId") Long parentId);
}
