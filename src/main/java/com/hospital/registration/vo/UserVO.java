package com.hospital.registration.vo;

import com.hospital.registration.common.Gender;
import com.hospital.registration.common.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @title: UserVO
 * @author: Su
 * @date: 2026/1/12 10:40
 * @version: 1.0
 * @description: 用户返回VO - 返回给前端的用户信息（不含密码）
 */
@Data
public class UserVO {

    // 用户ID
    private Long id;

    // 用户名
    private String username;

    // 真实姓名
    private String realName;

    // 身份证号（脱敏显示：330106********1234）
    private String idCard;

    // 手机号（脱敏显示：138****8000）
    private String phone;

    // 性别
    private Gender gender;

    // 出生日期
    private LocalDate birthDate;

    // 邮箱
    private String email;

    // 角色
    private UserRole role;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 注意：没有password字段！保护用户隐私
}
