package com.hospital.registration.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @title: AppLoginDTO
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端登录请求DTO
 */
@Data
public class AppLoginDTO {

    // 手机号（必填）
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    // 密码（必填）
    @NotBlank(message = "密码不能为空")
    private String password;
}

