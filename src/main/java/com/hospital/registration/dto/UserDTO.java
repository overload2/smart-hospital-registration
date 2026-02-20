package com.hospital.registration.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @title UserDTO
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 用户信息DTO（新增/修改）
 */
@Data
public class UserDTO {

    // 用户名
    private String username;

    // 密码（新增时必填）
    private String password;

    // 真实姓名
    private String realName;

    // 身份证号
    private String idCard;

    // 手机号
    private String phone;

    // 性别：MALE/FEMALE
    private String gender;

    // 出生日期
    private LocalDate birthDate;

    // 邮箱
    private String email;

    // 状态
    private Integer status;
}
