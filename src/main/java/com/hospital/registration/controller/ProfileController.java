package com.hospital.registration.controller;

import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.ChangePasswordDTO;
import com.hospital.registration.dto.ProfileDTO;
import com.hospital.registration.service.UserService;
import com.hospital.registration.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @title ProfileController
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 个人中心控制器
 */
@Slf4j
@RestController
@RequestMapping("/profile")
public class ProfileController {

    private final UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 获取当前用户信息
     */
    @GetMapping("/info")
    public Result getInfo(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取当前用户信息 - userId: {}", userId);
        UserVO user = userService.getCurrentUser(userId);
        return Result.ok().data("user", user);
    }

    /**
     * 修改个人信息
     */
    @PostMapping("/update")
    @OperationLog(module = "个人中心", operation = "UPDATE")
    public Result updateProfile(HttpServletRequest request, @RequestBody ProfileDTO profileDTO) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改个人信息 - userId: {}", userId);
        UserVO user = userService.updateProfile(userId, profileDTO);
        return Result.ok("修改成功").data("user", user);
    }

    /**
     * 修改密码
     */
    @PostMapping("/change-password")
    @OperationLog(module = "个人中心", operation = "UPDATE")
    public Result changePassword(HttpServletRequest request, @RequestBody ChangePasswordDTO changePasswordDTO) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("修改密码 - userId: {}", userId);
        userService.changePassword(userId, changePasswordDTO);
        return Result.ok("密码修改成功，请重新登录");
    }
}