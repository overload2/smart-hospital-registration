package com.hospital.registration.service;

import com.hospital.registration.dto.LoginDTO;
import com.hospital.registration.dto.RegisterDTO;
import com.hospital.registration.vo.UserVO;

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
}

