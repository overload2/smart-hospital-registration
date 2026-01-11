package com.hospital.registration.entity;

import com.hospital.registration.common.Gender;
import com.hospital.registration.common.UserRole;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

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
@Entity //@Entity：标记这是一个JPA实体类
@Table(name = "user") //@Table(name = "user")：指定表名
public class User {

    // 主键
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) //@GeneratedValue(strategy = GenerationType.IDENTITY)：主键自增
    private Long id;

    //@Column：列属性配置
    //    - nullable = false：不能为空
    //    - unique = true：唯一约束
    //    - length = 50：字段长度
    // 用户名
    @Column(nullable = false, unique = true, length = 50)
    private String username;

    // 密码
    @Column(nullable = false, length = 100)
    private String password;

    // 真实姓名
    @Column(name = "real_name", length = 50) //  name = "real_name"：数据库列名（驼峰转下划线）
    private String realName;

    // 身份证号
    @Column(name = "id_card", length = 18)
    private String idCard;

    // 手机号
    @Column(length = 11)
    private String phone;

    // 性别
    @Enumerated(EnumType.STRING)    //   @Enumerated(EnumType.STRING)：枚举类型，存储字符串值
    @Column(length = 10)
    private Gender gender;

    // 出生日期
    @Column(name = "birth_date")
    private LocalDate birthDate;

    // 邮箱
    @Column(length = 100)
    private String email;

    // 角色
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private UserRole role;

    // 状态 ：0-禁用，1-启用
    @Column(nullable = false)
    private Integer status = 1;  // 默认启用

    // 创建时间
    @CreationTimestamp //@CreationTimestamp：自动填充创建时间
    @Column(name = "create_time", updatable = false)
    private LocalDateTime createTime;

    // 更新时间
    @UpdateTimestamp //@UpdateTimestamp：自动更新修改时间
    @Column(name = "update_time")
    private LocalDateTime updateTime;
}


