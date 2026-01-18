package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hospital.registration.common.Gender;
import com.hospital.registration.common.UserRole;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * @title: User
 * @author: Su
 * @date: 2026/1/11 18:58
 * @version: 1.0
 * @description: 用户表
 */
@Data
@TableName("user")
public class User {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 用户名
    private String username;

    // 密码
    private String password;

    // 真实姓名
    @TableField("real_name")
    private String realName;

    // 身份证号
    @TableField("id_card")
    private String idCard;

    // 手机号
    private String phone;

    // 性别
    private Gender gender;

    // 出生日期
    @TableField("birth_date")
    private LocalDate birthDate;

    // 邮箱
    private String email;

    // 角色
    private UserRole role;

    // 状态：0-禁用，1-启用
    private Integer status = 1;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
