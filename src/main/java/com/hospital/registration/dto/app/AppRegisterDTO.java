package com.hospital.registration.dto.app;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @title: AppRegisterDTO
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端注册请求DTO
 */
@Data
public class AppRegisterDTO {

    // 手机号（必填）
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    // 密码（必填）
    @NotBlank(message = "密码不能为空")
    private String password;

    // 真实姓名（必填）
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    // 身份证号
    private String idCard;

    // 性别：MALE-男，FEMALE-女
    private String gender;
}
