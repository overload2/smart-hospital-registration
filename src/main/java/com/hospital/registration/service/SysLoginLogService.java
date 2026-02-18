package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.LoginLogQueryDTO;
import com.hospital.registration.vo.SysLoginLogVO;

/**
 * @title 登录日志Service
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 登录日志业务接口
 */
public interface SysLoginLogService {

    // 记录登录日志
    void recordLoginLog(Long userId, String username, String ip, String browser, String os, Integer status, String msg);

    // 分页查询登录日志
    Page<SysLoginLogVO> getLoginLogs(LoginLogQueryDTO dto);

    // 清理历史日志
    int cleanHistoryLogs(int days);
}