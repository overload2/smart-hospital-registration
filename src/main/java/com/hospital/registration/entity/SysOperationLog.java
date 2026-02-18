package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 操作日志实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 记录用户操作日志
 */
@Data
@TableName("sys_operation_log")
public class SysOperationLog {

    // 日志ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 操作用户ID
    private Long userId;

    // 操作用户名
    private String username;

    // 操作模块
    private String module;

    // 操作类型(ADD/UPDATE/DELETE/QUERY)
    private String operation;

    // 请求方法
    private String method;

    // 请求参数
    private String params;

    // 操作IP
    private String ip;

    // 操作状态(0失败 1成功)
    private Integer status;

    // 错误信息
    private String errorMsg;

    // 操作时间
    private LocalDateTime operationTime;

    // 耗时(毫秒)
    private Long costTime;
}