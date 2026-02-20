package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.Role;
import com.hospital.registration.entity.UserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: UserRoleMapper
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 用户角色关联Mapper
 */
@Mapper
public interface UserRoleMapper extends BaseMapper<UserRole> {

    /**
     * 根据用户ID查询角色列表
     *
     * @param userId 用户ID
     * @return 角色列表
     */
    List<Role> selectRolesByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID删除用户角色关联
     *
     * @param userId 用户ID
     * @return 删除数量
     */
    int deleteByUserId(@Param("userId") Long userId);

    /**
     * 查询用户的角色编码列表
     */
    List<String> selectRoleCodesByUserId(@Param("userId") Long userId);
}
