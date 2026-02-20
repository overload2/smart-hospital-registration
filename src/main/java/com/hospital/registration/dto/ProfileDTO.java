package com.hospital.registration.dto;

import lombok.Data;

import java.time.LocalDate;

/**
 * @title ProfileDTO
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 个人信息修改DTO
 */
@Data
public class ProfileDTO {

    // 真实姓名
    private String realName;

    // 手机号
    private String phone;

    // 邮箱
    private String email;

    // 性别
    private String gender;

    // 出生日期
    private LocalDate birthDate;

    // 身份证号
    private String idCard;
}
