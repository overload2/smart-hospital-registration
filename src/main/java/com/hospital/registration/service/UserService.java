package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.*;
import com.hospital.registration.vo.UserVO;

import java.util.List;
import java.util.Map;

/**
 * @title: UserService
 * @author: Su
 * @date: 2026/1/12 11:00
 * @version: 1.0
 * @description: 用户服务接口
 */
public interface UserService {

    /**
     * 用户注册
     * @param registerDTO 注册信息
     * @return 注册成功的用户信息
     */
    UserVO register(RegisterDTO registerDTO);

    /**
     * 用户登录
     * @param loginDTO 登录信息
     * @return Map包含token和用户信息
     */
    Map<String, Object> login(LoginDTO loginDTO);

    /**
     * 根据ID获取用户信息
     * @param userId 用户ID
     * @return 用户信息
     */
    UserVO getUserById(Long userId);


    /**
     * 根据角色编码获取用户列表
     * @param roleCode 角色编码
     * @return 用户列表
     */
    List<UserVO> getUsersByRoleCode(String roleCode);

    /**
     * 分页查询用户列表
     */
    Page<UserVO> getUserPage(UserQueryDTO queryDTO);

    /**
     * 新增用户
     */
    UserVO createUser(UserDTO userDTO);

    /**
     * 修改用户
     */
    UserVO updateUser(Long id, UserDTO userDTO);

    /**
     * 删除用户
     */
    void deleteUser(Long id);

    /**
     * 批量删除用户
     */
    void batchDeleteUser(List<Long> ids);

    /**
     * 更新用户状态
     */
    void updateUserStatus(Long id, Integer status);

    /**
     * 批量更新用户状态
     */
    void batchUpdateUserStatus(List<Long> ids, Integer status);

    /**
     * 重置用户密码
     */
    void resetPassword(Long id, String newPassword);

    /**
     * 为用户分配角色
     */
    void assignRoles(Long userId, List<Long> roleIds);

    /**
     * 获取当前用户信息（个人中心）
     */
    UserVO getCurrentUser(Long userId);

    /**
     * 修改个人信息
     */
    UserVO updateProfile(Long userId, ProfileDTO profileDTO);

    /**
     * 修改密码
     */
    void changePassword(Long userId, ChangePasswordDTO changePasswordDTO);
}

