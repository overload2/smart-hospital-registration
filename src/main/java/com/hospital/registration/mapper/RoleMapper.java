package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
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
}
