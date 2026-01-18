package com.hospital.registration.controller;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.LoginDTO;
import com.hospital.registration.dto.RegisterDTO;
import com.hospital.registration.service.UserService;
import com.hospital.registration.vo.UserVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @title: UserController
 * @author: Su
 * @date: 2026/1/12 11:30
 * @version: 1.0
 * @description: 用户控制器 - 处理用户相关请求
 */
@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    // 构造器注入
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 用户注册
     * POST /api/hospital/user/register
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody RegisterDTO registerDTO) {
        log.info("收到注册请求 - 用户名: {}", registerDTO.getUsername());

        // 调用Service处理业务
        UserVO userVO = userService.register(registerDTO);

        // 返回成功结果
        return Result.ok("注册成功").data("user", userVO);
    }

    /**
     * 用户登录
     * POST /api/hospital/user/login
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        log.info("收到登录请求 - 用户名: {}", loginDTO.getUsername());

        // 调用Service处理业务
        Map<String, Object> result = userService.login(loginDTO);

        // 返回成功结果（包含token和用户信息）
        return Result.ok("登录成功")
                .data("token", result.get("token"))
                .data("user", result.get("user"));
    }

    /**
     * 获取用户信息
     * GET /api/hospital/user/{userId}
     */
    @GetMapping("/{userId}")
    public Result getUserInfo(@PathVariable Long userId) {
        log.info("获取用户信息 - ID: {}", userId);

        UserVO userVO = userService.getUserById(userId);

        return Result.ok().data("user", userVO);
    }

    /**
     * 测试接口
     * GET /api/hospital/user/test
     */
    @GetMapping("/test")
    public Result test() {
        return Result.ok("用户模块接口正常");
    }
}

