package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.OperationLogQueryDTO;
import com.hospital.registration.entity.SysOperationLog;
import com.hospital.registration.mapper.SysOperationLogMapper;
import com.hospital.registration.service.SysOperationLogService;
import com.hospital.registration.vo.SysOperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @title 操作日志Service实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志业务实现
 */
@Service
public class SysOperationLogServiceImpl implements SysOperationLogService {

    @Autowired
    private SysOperationLogMapper sysOperationLogMapper;

    /**
     * 记录操作日志(异步)
     *
     * @param log 操作日志实体
     */
    @Override
    @Async
    public void recordLog(SysOperationLog log) {
        sysOperationLogMapper.insert(log);
    }

    /**
     * 分页查询操作日志
     *
     * @param dto 查询条件DTO(包含用户名、模块、操作类型、状态、时间范围、分页参数)
     * @return 操作日志VO分页对象
     */
    @Override
    public Page<SysOperationLogVO> getOperationLogs(OperationLogQueryDTO dto) {
        Page<SysOperationLogVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return sysOperationLogMapper.selectPageByCondition(
                page,
                dto.getUsername(),
                dto.getModule(),
                dto.getOperation(),
                dto.getStatus(),
                dto.getStartTime(),
                dto.getEndTime()
        );
    }

    /**
     * 清理历史日志
     *
     * @param days 保留天数，删除该天数之前的日志
     * @return 删除的记录数
     */
    @Override
    public int cleanHistoryLogs(int days) {
        return sysOperationLogMapper.deleteByDaysBefore(days);
    }
}