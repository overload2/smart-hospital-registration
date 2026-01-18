package com.hospital.registration.service.impl;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.common.UserRole;
import com.hospital.registration.dto.LoginDTO;
import com.hospital.registration.dto.RegisterDTO;
import com.hospital.registration.entity.User;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.service.UserService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.utils.PasswordUtil;
import com.hospital.registration.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.Map;

/**
 * @title: UserServiceImpl
 * @author: Su
 * @date: 2026/1/12 11:10
 * @version: 1.0
 * @description: 用户服务实现类
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final PasswordUtil passwordUtil;
    private final JwtUtil jwtUtil;

    // 构造器注入（推荐方式）
    public UserServiceImpl(UserMapper userMapper,
                           PasswordUtil passwordUtil,
                           JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 用户注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(RegisterDTO registerDTO) {
        log.info("用户注册 - 用户名: {}", registerDTO.getUsername());

        // 1. 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(registerDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
        }

        // 2. 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(registerDTO, user);  // 复制属性

        // 3. 加密密码
        String encodedPassword = passwordUtil.encode(registerDTO.getPassword());
        user.setPassword(encodedPassword);

        // 4. 设置默认值
        user.setRole(UserRole.PATIENT);  // 默认角色：患者
        user.setStatus(1);  // 默认启用

        // 5. 保存到数据库
        userMapper.insert(user);
        User savedUser = user;

        log.info("用户注册成功 - ID: {}, 用户名: {}", savedUser.getId(), savedUser.getUsername());

        // 6. 转换为VO返回
        return convertToVO(savedUser);
    }

    /**
     * 用户登录
     */
    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        log.info("用户登录 - 用户名: {}", loginDTO.getUsername());

        // 1. 查询用户
        User user = userMapper.selectByUsername(loginDTO.getUsername());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 2. 验证密码
        boolean isPasswordCorrect = passwordUtil.matches(loginDTO.getPassword(), user.getPassword());
        if (!isPasswordCorrect) {
            throw new BusinessException(ResultCode.PASSWORD_ERROR);
        }

        // 3. 检查账号状态
        if (user.getStatus() == 0) {
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "账号已被禁用");
        }

        // 4. 生成Token
        String token = jwtUtil.generateToken(user.getId(), user.getUsername());

        log.info("用户登录成功 - ID: {}, 用户名: {}", user.getId(), user.getUsername());

        // 5. 返回Token和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", convertToVO(user));

        return result;
    }

    /**
     * 根据ID获取用户信息
     */
    @Override
    public UserVO getUserById(Long userId) {
        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        return convertToVO(user);
    }

    /**
     * Entity转VO（私有方法）
     */
    private UserVO convertToVO(User user) {
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);  // 复制属性（不包含password）
        return userVO;
    }
}
