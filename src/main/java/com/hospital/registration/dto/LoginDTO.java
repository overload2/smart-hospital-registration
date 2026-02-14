package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title: LoginDTO
 * @author: Su
 * @date: 2026/1/12 10:30
 * @version: 1.0
 * @description: 用户登录DTO - 接收前端登录请求参数
 */
@Data
public class LoginDTO {

    // 用户名（必填）
    @NotBlank(message = "用户名不能为空")
    private String username;

    // 密码（必填）
    @NotBlank(message = "密码不能为空")
    private String password;
}

