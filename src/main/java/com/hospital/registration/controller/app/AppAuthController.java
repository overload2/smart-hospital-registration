package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.app.AppLoginDTO;
import com.hospital.registration.dto.app.AppRegisterDTO;
import com.hospital.registration.service.TokenService;
import com.hospital.registration.service.UserService;
import com.hospital.registration.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * @title: AppAuthController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端认证控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/auth")
public class AppAuthController {

    private final UserService userService;
    private final TokenService tokenService;

    /**
     * 构造器注入
     */
    public AppAuthController(UserService userService, TokenService tokenService) {
        this.userService = userService;
        this.tokenService = tokenService;
    }

    /**
     * 手机号密码登录
     */
    @PostMapping("/login")
    public Result login(@Valid @RequestBody AppLoginDTO loginDTO) {
        log.info("患者端登录 - 手机号: {}", loginDTO.getPhone());
        Map<String, Object> result = userService.appLogin(loginDTO);
        return Result.ok("登录成功")
                .data("token", result.get("token"))
                .data("user", result.get("user"));
    }

    /**
     * 患者注册
     */
    @PostMapping("/register")
    public Result register(@Valid @RequestBody AppRegisterDTO registerDTO) {
        log.info("患者端注册 - 手机号: {}", registerDTO.getPhone());
        UserVO userVO = userService.appRegister(registerDTO);
        return Result.ok("注册成功").data("user", userVO);
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/user-info")
    public Result getUserInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取用户信息 - 用户ID: {}", userId);
        UserVO vo=userService.getUserById(userId);
        return Result.ok().data("user", vo);
    }

    /**
     * 更新用户信息
     */
    @PostMapping("/update-info")
    public Result updateUserInfo(@RequestBody Map<String, Object> params, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("更新用户信息 - 用户ID: {}", userId);

        String realName = (String) params.get("realName");
        String gender = (String) params.get("gender");  // String 类型
        String idCard = (String) params.get("idCard");

        userService.updateUserInfo(userId, realName, gender, idCard);
        return Result.ok("更新成功");
    }

    /**
     * 退出登录
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("用户退出登录 - 用户ID: {}", userId);
        // 删除Redis中的token
        tokenService.removeToken(userId, false);
        return Result.ok("退出成功");
    }

}
