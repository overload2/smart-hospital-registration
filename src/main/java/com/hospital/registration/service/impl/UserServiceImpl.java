package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.Gender;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.*;
import com.hospital.registration.dto.app.AppLoginDTO;
import com.hospital.registration.dto.app.AppRegisterDTO;
import com.hospital.registration.entity.Role;
import com.hospital.registration.entity.User;
import com.hospital.registration.entity.UserRole;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.mapper.UserRoleMapper;
import com.hospital.registration.service.PermissionService;
import com.hospital.registration.service.TokenService;
import com.hospital.registration.service.UserService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.utils.PasswordUtil;
import com.hospital.registration.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    private final UserRoleMapper userRoleMapper;
    private final TokenService tokenService;
    private final PermissionService permissionService;

    /**
     * 构造器注入
     */
    public UserServiceImpl(UserMapper userMapper,
                           PasswordUtil passwordUtil,
                           JwtUtil jwtUtil,
                           UserRoleMapper userRoleMapper,
                           TokenService tokenService,
                           PermissionService permissionService) {
        this.userMapper = userMapper;
        this.passwordUtil = passwordUtil;
        this.jwtUtil = jwtUtil;
        this.userRoleMapper = userRoleMapper;
        this.tokenService = tokenService;
        this.permissionService = permissionService;
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
        user.setStatus(1);  // 默认启用
        user.setGender(Gender.MALE);
        //注册来源
        String source =registerDTO.getSource();
        Long roleId = null;
        if("miniapp".equals(source)){
            roleId = 4L;
            user.setRole(com.hospital.registration.common.UserRole.PATIENT);
            log.info("小程序注册");
        }else {
            roleId = 5L;
            user.setRole(com.hospital.registration.common.UserRole.USER);
            log.info("web注册");
        }

        // 5. 保存到数据库
        userMapper.insert(user);
        // 6. 根据注册来源分配不同角色
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(roleId);


        userRoleMapper.insert(userRole);
        log.info("用户注册成功 - ID: {}, 用户名: {}", user.getId(), user.getUsername());

        // 6. 转换为VO返回
        return convertToVO(user);
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

        // 5. 缓存Token
        tokenService.saveToken(user.getId(), token, true);
        log.info("用户登录成功 - ID: {}, 用户名: {}", user.getId(), user.getUsername());

        // 6. 返回Token和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        // 获取用户角色和权限
        UserVO userVO = convertToVO(user);
        List<String> roleCodes = userRoleMapper.selectRoleCodesByUserId(user.getId());
        List<String> permissions = permissionService.getUserPermissionCodes(user.getId());
        userVO.setRoleCodes(roleCodes);
        userVO.setPermissions(permissions);
        result.put("user", userVO);

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

    /**
     * 根据角色编码获取用户列表
     */
    @Override
    public List<UserVO> getUsersByRoleCode(String roleCode) {
        log.info("根据角色编码查询用户列表 - roleCode: {}", roleCode);
        return userMapper.selectUsersByRoleCode(roleCode);
    }

    /**
     * 分页查询用户列表
     */
    @Override
    public Page<UserVO> getUserPage(UserQueryDTO queryDTO) {
        log.info("分页查询用户列表");
        Page<UserVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        Page<UserVO> result = userMapper.selectPageByCondition(
                page,
                queryDTO.getUsername(),
                queryDTO.getRealName(),
                queryDTO.getPhone(),
                queryDTO.getStatus()
        );
        // 填充状态名称和性别名称
        for (UserVO vo : result.getRecords()) {
            vo.setStatusName(vo.getStatus() == 1 ? "启用" : "禁用");
            if (vo.getGender() != null) {
                vo.setGenderName(vo.getGender().getName());
            }
            // 查询用户角色
            List<Role> roles = userRoleMapper.selectRolesByUserId(vo.getId());
            List<String> roleNames = new ArrayList<>();
            for (Role role : roles) {
                roleNames.add(role.getRoleName());
            }
            vo.setRoleNames(roleNames);
        }
        return result;
    }

    /**
     * 新增用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO createUser(UserDTO userDTO) {
        log.info("新增用户 - 用户名: {}", userDTO.getUsername());

        // 检查用户名是否已存在
        User existUser = userMapper.selectByUsername(userDTO.getUsername());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
        }

        // 创建用户实体
        User user = new User();
        BeanUtils.copyProperties(userDTO, user);

        // 加密密码
        String encodedPassword = passwordUtil.encode(userDTO.getPassword());
        user.setPassword(encodedPassword);

        // 设置默认状态
        if (user.getStatus() == null) {
            user.setStatus(1);
        }

        // 保存用户
        userMapper.insert(user);
        log.info("新增用户成功 - ID: {}", user.getId());

        return convertToVO(user);
    }

    /**
     * 修改用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUser(Long id, UserDTO userDTO) {
        log.info("修改用户 - ID: {}", id);

        // 检查用户是否存在
        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 如果修改了用户名，检查是否重复
        if (userDTO.getUsername() != null && !userDTO.getUsername().equals(user.getUsername())) {
            User existUser = userMapper.selectByUsername(userDTO.getUsername());
            if (existUser != null) {
                throw new BusinessException(ResultCode.USER_ALREADY_EXIST);
            }
        }

        // 更新用户信息（不更新密码）
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setUsername(userDTO.getUsername());
        updateUser.setRealName(userDTO.getRealName());
        updateUser.setIdCard(userDTO.getIdCard());
        updateUser.setPhone(userDTO.getPhone());
        updateUser.setEmail(userDTO.getEmail());
        updateUser.setBirthDate(userDTO.getBirthDate());
        if (userDTO.getGender() != null) {
            updateUser.setGender(com.hospital.registration.common.Gender.valueOf(userDTO.getGender()));
        }
        updateUser.setStatus(userDTO.getStatus());

        userMapper.updateById(updateUser);
        log.info("修改用户成功 - ID: {}", id);

        return getUserById(id);
    }

    /**
     * 删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUser(Long id) {
        log.info("删除用户 - ID: {}", id);

        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 逻辑删除
        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setDeleted(1);
        userMapper.updateById(updateUser);

        log.info("删除用户成功 - ID: {}", id);
    }

    /**
     * 批量删除用户
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchDeleteUser(List<Long> ids) {
        log.info("批量删除用户 - ids: {}", ids);
        for (Long id : ids) {
            deleteUser(id);
        }
    }

    /**
     * 更新用户状态
     */
    @Override
    public void updateUserStatus(Long id, Integer status) {
        log.info("更新用户状态 - ID: {}, status: {}", id, status);

        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        User updateUser = new User();
        updateUser.setId(id);
        updateUser.setStatus(status);
        userMapper.updateById(updateUser);
    }

    /**
     * 批量更新用户状态
     */
    @Override
    public void batchUpdateUserStatus(List<Long> ids, Integer status) {
        log.info("批量更新用户状态 - ids: {}, status: {}", ids, status);
        userMapper.batchUpdateStatus(ids, status);
    }

    /**
     * 重置用户密码
     */
    @Override
    public void resetPassword(Long id, String newPassword) {
        log.info("重置用户密码 - ID: {}", id);

        User user = userMapper.selectById(id);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        String encodedPassword = passwordUtil.encode(newPassword);
        userMapper.updatePassword(id, encodedPassword);
        log.info("重置密码成功 - ID: {}", id);
    }

    /**
     * 为用户分配角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRoles(Long userId, List<Long> roleIds) {
        log.info("为用户分配角色 - userId: {}, roleIds: {}", userId, roleIds);

        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 先删除原有角色
        userRoleMapper.deleteByUserId(userId);

        // 批量插入新角色
        for (Long roleId : roleIds) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }

        log.info("分配角色成功 - userId: {}", userId);
    }

    /**
     * 获取当前用户信息（个人中心）
     */
    @Override
    public UserVO getCurrentUser(Long userId) {
        log.info("获取当前用户信息 - userId: {}", userId);
        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }
        UserVO vo = convertToVO(user);
        // 填充角色信息
        List<Role> roles = userRoleMapper.selectRolesByUserId(userId);
        List<String> roleNames = new ArrayList<>();
        for (Role role : roles) {
            roleNames.add(role.getRoleName());
        }
        vo.setRoleNames(roleNames);
        vo.setStatusName(vo.getStatus() == 1 ? "启用" : "禁用");
        if (vo.getGender() != null) {
            vo.setGenderName(vo.getGender().getName());
        }
        return vo;
    }

    /**
     * 修改个人信息
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateProfile(Long userId, ProfileDTO profileDTO) {
        log.info("修改个人信息 - userId: {}", userId);

        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setRealName(profileDTO.getRealName());
        updateUser.setPhone(profileDTO.getPhone());
        updateUser.setEmail(profileDTO.getEmail());
        updateUser.setIdCard(profileDTO.getIdCard());
        updateUser.setBirthDate(profileDTO.getBirthDate());
        if (profileDTO.getGender() != null) {
            updateUser.setGender(com.hospital.registration.common.Gender.valueOf(profileDTO.getGender()));
        }

        userMapper.updateById(updateUser);
        log.info("修改个人信息成功 - userId: {}", userId);

        return getCurrentUser(userId);
    }

    /**
     * 修改密码
     */
    @Override
    public void changePassword(Long userId, ChangePasswordDTO changePasswordDTO) {
        log.info("修改密码 - userId: {}", userId);

        // 验证新密码和确认密码是否一致
        if (!changePasswordDTO.getNewPassword().equals(changePasswordDTO.getConfirmPassword())) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "两次输入的密码不一致");
        }

        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST);
        }

        // 验证旧密码
        boolean isPasswordCorrect = passwordUtil.matches(changePasswordDTO.getOldPassword(), user.getPassword());
        if (!isPasswordCorrect) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "原密码错误");
        }

        // 更新密码
        String encodedPassword = passwordUtil.encode(changePasswordDTO.getNewPassword());
        userMapper.updatePassword(userId, encodedPassword);
        log.info("修改密码成功 - userId: {}", userId);
    }

    /**
     * 患者端登录（手机号+密码）
     */
    @Override
    public Map<String, Object> appLogin(AppLoginDTO loginDTO) {
        log.info("患者端登录 - 手机号: {}", loginDTO.getPhone());

        // 1. 根据手机号查询用户
        User user = userMapper.selectByPhone(loginDTO.getPhone());
        if (user == null) {
            throw new BusinessException(ResultCode.USER_NOT_EXIST.getCode(), "该手机号未注册");
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

        // 5. 缓存Token
        tokenService.saveToken(user.getId(), token, false);
        log.info("患者端登录成功 - ID: {}, 手机号: {}", user.getId(), loginDTO.getPhone());

        // 6. 返回Token和用户信息
        Map<String, Object> result = new HashMap<>();
        result.put("token", token);
        result.put("user", convertToVO(user));

        return result;
    }

    /**
     * 患者端注册
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO appRegister(AppRegisterDTO registerDTO) {
        log.info("患者端注册 - 手机号: {}", registerDTO.getPhone());

        // 1. 检查手机号是否已注册
        User existUser = userMapper.selectByPhone(registerDTO.getPhone());
        if (existUser != null) {
            throw new BusinessException(ResultCode.USER_ALREADY_EXIST.getCode(), "该手机号已注册");
        }

        // 2. 创建用户实体
        User user = new User();
        user.setPhone(registerDTO.getPhone());
        user.setUsername(registerDTO.getPhone());  // 用手机号作为用户名
        user.setRealName(registerDTO.getRealName());
        user.setIdCard(registerDTO.getIdCard());

        // 3. 设置性别
        if (registerDTO.getGender() != null && !registerDTO.getGender().isEmpty()) {
            user.setGender(Gender.valueOf(registerDTO.getGender()));
        } else {
            user.setGender(Gender.MALE);  // 默认男
        }

        // 4. 加密密码
        String encodedPassword = passwordUtil.encode(registerDTO.getPassword());
        user.setPassword(encodedPassword);

        // 5. 设置默认值
        user.setStatus(1);  // 默认启用
        user.setRole(com.hospital.registration.common.UserRole.PATIENT);  // 患者角色

        // 6. 保存到数据库
        userMapper.insert(user);

        // 7. 分配患者角色（角色ID=4）
        UserRole userRole = new UserRole();
        userRole.setUserId(user.getId());
        userRole.setRoleId(4L);
        userRoleMapper.insert(userRole);

        log.info("患者端注册成功 - ID: {}, 手机号: {}", user.getId(), registerDTO.getPhone());

        // 8. 转换为VO返回
        return convertToVO(user);
    }

    /**
     * 更新用户基本信息（患者端）
     */
    @Override
    public void updateUserInfo(Long userId, String realName, String gender, String idCard) {
        log.info("更新用户基本信息 - 用户ID: {}", userId);

        User user = userMapper.selectById(userId);
        if (user == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        User updateUser = new User();
        updateUser.setId(userId);
        updateUser.setRealName(realName);
        if (gender != null && !gender.isEmpty()) {
            updateUser.setGender(Gender.valueOf(gender));
        }
        updateUser.setIdCard(idCard);

        userMapper.updateById(updateUser);
        log.info("用户信息更新成功 - 用户ID: {}", userId);
    }
}
