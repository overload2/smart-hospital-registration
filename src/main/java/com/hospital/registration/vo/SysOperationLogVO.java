package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 操作日志VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志返回信息
 */
@Data
public class SysOperationLogVO {

    // 日志ID
    private Long id;

    // 操作用户名
    private String username;

    // 操作模块
    private String module;

    // 操作类型
    private String operation;

    // 请求方法
    private String method;

    // 请求参数
    private String params;

    // 操作IP
    private String ip;

    // 操作状态
    private Integer status;

    // 错误信息
    private String errorMsg;

    // 操作时间
    private LocalDateTime operationTime;

    // 耗时(毫秒)
    private Long costTime;
}
