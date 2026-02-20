package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: RoleMapper
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色Mapper
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据角色编码查询角色
     *
     * @param roleCode 角色编码
     * @return 角色实体
     */
    Role selectByRoleCode(@Param("roleCode") String roleCode);

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    List<Role> selectAllRoles();

    /**
     * 分页查询角色列表
     * @param page 分页对象
     * @param roleName 角色名称
     * @param roleCode 角色编码
     * @param status 状态
     * @return 分页结果
     */
    Page<Role> selectRolePage(Page<Role> page,
                              @Param("roleName") String roleName,
                              @Param("roleCode") String roleCode,
                              @Param("status") Integer status);

    /**
     * 批量更新角色状态
     * @param ids 角色ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
