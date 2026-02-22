package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 登录日志实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 记录用户登录日志
 */
@Data
@TableName("sys_login_log")
public class SysLoginLog {

    // 日志ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 用户ID
    private Long userId;

    // 用户名
    private String username;

    // 登录IP
    private String ip;

    // 登录地点
    private String location;

    // 浏览器
    private String browser;

    // 操作系统
    private String os;

    // 登录状态(0失败 1成功)
    private Integer status;

    // 提示消息
    private String msg;

    // 登录时间
    private LocalDateTime loginTime;
}
