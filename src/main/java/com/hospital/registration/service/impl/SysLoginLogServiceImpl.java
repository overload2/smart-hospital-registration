package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.LoginLogQueryDTO;
import com.hospital.registration.entity.SysLoginLog;
import com.hospital.registration.mapper.SysLoginLogMapper;
import com.hospital.registration.service.SysLoginLogService;
import com.hospital.registration.vo.SysLoginLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * @title 登录日志Service实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 登录日志业务实现
 */
@Service
public class SysLoginLogServiceImpl implements SysLoginLogService {

    @Autowired
    private SysLoginLogMapper sysLoginLogMapper;

    /**
     * 记录登录日志(异步)
     *
     * @param userId 用户ID
     * @param username 用户名
     * @param ip 登录IP
     * @param browser 浏览器
     * @param os 操作系统
     * @param status 登录状态(0失败 1成功)
     * @param msg 提示消息
     */
    @Override
    @Async
    public void recordLoginLog(Long userId, String username, String ip, String browser, String os, Integer status, String msg) {
        SysLoginLog log = new SysLoginLog();
        log.setUserId(userId);
        log.setUsername(username);
        log.setIp(ip);
        log.setBrowser(browser);
        log.setOs(os);
        log.setStatus(status);
        log.setMsg(msg);
        sysLoginLogMapper.insert(log);
    }

    /**
     * 分页查询登录日志
     *
     * @param dto 查询条件DTO(包含用户名、状态、IP、时间范围、分页参数)
     * @return 登录日志VO分页对象
     */
    @Override
    public Page<SysLoginLogVO> getLoginLogs(LoginLogQueryDTO dto) {
        Page<SysLoginLogVO> page = new Page<>(dto.getPageNum(), dto.getPageSize());
        return sysLoginLogMapper.selectPageByCondition(
                page,
                dto.getUsername(),
                dto.getStatus(),
                dto.getIp(),
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
        return sysLoginLogMapper.deleteByDaysBefore(days);
    }
}
