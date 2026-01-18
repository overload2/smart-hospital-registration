package com.hospital.registration.dto;

import com.hospital.registration.common.Gender;
import jakarta.validation.constraints.*;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDate;

/**
 * @title: RegisterDTO
 * @author: Su
 * @date: 2026/1/12 10:20
 * @version: 1.0
 * @description: 用户注册DTO - 接收前端注册请求参数
 */
@Data
public class RegisterDTO {

    // 用户名（必填，3-20位）
    @NotBlank(message = "用户名不能为空")
    @Size(min = 3, max = 20, message = "用户名长度必须在3-20位之间")
    private String username;

    // 密码（必填，6-20位）
    @NotBlank(message = "密码不能为空")
    @Size(min = 6, max = 20, message = "密码长度必须在6-20位之间")
    private String password;

    // 真实姓名（必填）
    @NotBlank(message = "真实姓名不能为空")
    private String realName;

    // 身份证号（必填，18位）
    @NotBlank(message = "身份证号不能为空")
    @Pattern(regexp = "^[1-9]\\d{5}(18|19|20)\\d{2}(0[1-9]|1[0-2])(0[1-9]|[12]\\d|3[01])\\d{3}[0-9Xx]$",
            message = "身份证号格式不正确")
    private String idCard;

    // 手机号（必填，11位）
    @NotBlank(message = "手机号不能为空")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "手机号格式不正确")
    private String phone;

    // 性别（可选）
    private Gender gender;

    // 出生日期（可选）
    private LocalDate birthDate;

    // 邮箱（可选，但如果填写必须格式正确）
    @Email(message = "邮箱格式不正确")
    private String email;
}

