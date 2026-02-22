package com.hospital.registration.controller;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.LoginDTO;
import com.hospital.registration.dto.RegisterDTO;
import com.hospital.registration.service.SysLoginLogService;
import com.hospital.registration.service.TokenService;
import com.hospital.registration.service.UserService;
import com.hospital.registration.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    private final SysLoginLogService sysLoginLogService;

    private final TokenService tokenService;

    // 构造器注入
    public UserController(UserService userService, SysLoginLogService sysLoginLogService, TokenService tokenService) {
        this.userService = userService;
        this.sysLoginLogService = sysLoginLogService;
        this.tokenService = tokenService;
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
    public Result login(@Valid @RequestBody LoginDTO loginDTO, HttpServletRequest request) {
        log.info("收到登录请求 - 用户名: {}", loginDTO.getUsername());

        //获取客户端信息
        String ip =getIpAddress(request);
        String userAgent = request.getHeader("User-Agent");
        String browser = parseBrowser(userAgent);
        String os = parseOs(userAgent);
        try {
            // 调用Service处理业务
            Map<String, Object> result = userService.login(loginDTO);
            UserVO userVO = (UserVO) result.get("user");
            //记录登录成功日志
            sysLoginLogService.recordLoginLog(userVO.getId(), loginDTO.getUsername(), ip, browser, os, 1, "登录成功");

            // 返回成功结果（包含token和用户信息）
            return Result.ok("登录成功")
                    .data("token", result.get("token"))
                    .data("user", result.get("user"));
        } catch (Exception e){
            //记录登录失败日志
            sysLoginLogService.recordLoginLog(null, loginDTO.getUsername(), ip, browser, os, 0, "登录失败");
            throw e;
        }

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
     * 根据角色编码获取用户列表
     * GET /api/hospital/user/list-by-role?roleCode=USER
     */
    @GetMapping("/list-by-role")
    public Result getUsersByRoleCode(@RequestParam String roleCode) {
        log.info("根据角色编码查询用户列表 - roleCode: {}", roleCode);
        List<UserVO> users = userService.getUsersByRoleCode(roleCode);
        return Result.ok().data("users", users);
    }

    /**
     * 用户登出
     * POST /api/hospital/user/logout
     */
    @PostMapping("/logout")
    public Result logout(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("管理端用户登出 - 用户ID: {}", userId);
        // 删除Redis中的Token
        tokenService.removeToken(userId, true);
        return Result.ok("登出成功");
    }

    /**
     * 测试接口
     * GET /api/hospital/user/test
     */
    @GetMapping("/test")
    public Result test() {
        return Result.ok("用户模块接口正常");
    }

    /**
     * 获取客户端IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }

    /**
     * 解析浏览器信息
     */
    private String parseBrowser(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }
        if (userAgent.contains("Edge")) {
            return "Edge";
        } else if (userAgent.contains("Chrome")) {
            return "Chrome";
        } else if (userAgent.contains("Firefox")) {
            return "Firefox";
        } else if (userAgent.contains("Safari")) {
            return "Safari";
        } else if (userAgent.contains("MSIE") || userAgent.contains("Trident")) {
            return "IE";
        }
        return "Other";
    }

    /**
     * 解析操作系统信息
     */
    private String parseOs(String userAgent) {
        if (userAgent == null) {
            return "Unknown";
        }
        if (userAgent.contains("Windows")) {
            return "Windows";
        } else if (userAgent.contains("Mac")) {
            return "MacOS";
        } else if (userAgent.contains("Linux")) {
            return "Linux";
        } else if (userAgent.contains("Android")) {
            return "Android";
        } else if (userAgent.contains("iPhone") || userAgent.contains("iPad")) {
            return "iOS";
        }
        return "Other";
    }
}

