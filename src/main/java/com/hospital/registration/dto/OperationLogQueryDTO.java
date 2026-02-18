package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title 操作日志查询DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志查询条件
 */
@Data
public class OperationLogQueryDTO {

    // 操作用户名
    private String username;

    // 操作模块
    private String module;

    // 操作类型
    private String operation;

    // 操作状态
    private Integer status;

    // 开始时间
    private String startTime;

    // 结束时间
    private String endTime;

    // 页码
    private Integer pageNum = 1;

    // 每页数量
    private Integer pageSize = 10;
}