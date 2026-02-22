package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title 登录日志查询DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 登录日志查询条件
 */
@Data
public class LoginLogQueryDTO {

    // 用户名
    private String username;

    // 登录状态
    private Integer status;

    // 登录IP
    private String ip;

    // 开始时间
    private String startTime;

    // 结束时间
    private String endTime;

    // 页码
    private Integer pageNum = 1;

    // 每页数量
    private Integer pageSize = 10;
}
