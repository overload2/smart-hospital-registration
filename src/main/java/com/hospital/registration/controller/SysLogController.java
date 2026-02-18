package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.LoginLogQueryDTO;
import com.hospital.registration.dto.OperationLogQueryDTO;
import com.hospital.registration.service.SysLoginLogService;
import com.hospital.registration.service.SysOperationLogService;
import com.hospital.registration.vo.SysLoginLogVO;
import com.hospital.registration.vo.SysOperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * @title 系统日志Controller
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志和登录日志接口
 */
@RestController
@RequestMapping("/sys/log")
public class SysLogController {

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @Autowired
    private SysLoginLogService sysLoginLogService;

    /**
     * 分页查询操作日志
     */
    @PostMapping("/operation/page")
    public Result operationLogPage(@RequestBody OperationLogQueryDTO dto) {
        Page<SysOperationLogVO> page = sysOperationLogService.getOperationLogs(dto);
        return Result.ok().data("page", page);
    }

    /**
     * 清理操作日志
     */
    @PostMapping("/operation/clean")
    @OperationLog(module = "日志管理", operation = "DELETE")
    public Result cleanOperationLog(@RequestParam(defaultValue = "90") Integer days) {
        int count = sysOperationLogService.cleanHistoryLogs(days);
        return Result.ok().message("清理成功").data("count", count);
    }

    /**
     * 分页查询登录日志
     */
    @PostMapping("/login/page")
    public Result loginLogPage(@RequestBody LoginLogQueryDTO dto) {
        Page<SysLoginLogVO> page = sysLoginLogService.getLoginLogs(dto);
        return Result.ok().data("page", page);
    }

    /**
     * 清理登录日志
     */
    @PostMapping("/login/clean")
    @OperationLog(module = "日志管理", operation = "DELETE")
    public Result cleanLoginLog(@RequestParam(defaultValue = "90") Integer days) {
        int count = sysLoginLogService.cleanHistoryLogs(days);
        return Result.ok().message("清理成功").data("count", count);
    }
}
