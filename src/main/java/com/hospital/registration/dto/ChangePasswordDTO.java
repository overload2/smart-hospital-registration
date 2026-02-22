package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title ChangePasswordDTO
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 修改密码DTO
 */
@Data
public class ChangePasswordDTO {

    // 旧密码
    private String oldPassword;

    // 新密码
    private String newPassword;

    // 确认密码
    private String confirmPassword;
}
