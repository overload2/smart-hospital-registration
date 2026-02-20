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

    /**
     * 查询直接子权限ID
     *
     * @param parentId 父权限ID
     * @return 直接子权限ID列表
     */
    List<Long> selectDirectChildrenIds(@Param("parentId") Long parentId);

    /**
     * 根据ID查询父权限ID
     *
     * @param id 权限ID
     * @return 父权限ID
     */
    Long selectParentIdById(@Param("id") Long id);

    /**
     * 批量更新权限状态
     *
     * @param ids    权限ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
