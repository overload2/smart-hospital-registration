package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 登录日志VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 登录日志返回信息
 */
@Data
public class SysLoginLogVO {

    // 日志ID
    private Long id;

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

    // 登录状态
    private Integer status;

    // 提示消息
    private String msg;

    // 登录时间
    private LocalDateTime loginTime;
}
