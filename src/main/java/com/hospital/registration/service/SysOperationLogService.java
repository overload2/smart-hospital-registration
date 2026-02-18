package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.OperationLogQueryDTO;
import com.hospital.registration.entity.SysOperationLog;
import com.hospital.registration.vo.SysOperationLogVO;

/**
 * @title 操作日志Service
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志业务接口
 */
public interface SysOperationLogService {

    // 记录操作日志
    void recordLog(SysOperationLog log);

    // 分页查询操作日志
    Page<SysOperationLogVO> getOperationLogs(OperationLogQueryDTO dto);

    // 清理历史日志
    int cleanHistoryLogs(int days);
}
