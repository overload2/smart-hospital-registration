package com.hospital.registration.service;

import com.hospital.registration.vo.DashboardStatsVO;

import java.util.List;
import java.util.Map;

/**
 * @title: DashboardService
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 首页仪表盘服务接口
 */
public interface DashboardService {

    /**
     * 获取首页统计数据
     */
    DashboardStatsVO getStats();

    /**
     * 获取近7天挂号趋势
     */
    List<Map<String, Object>> getRegistrationTrend();

    /**
     * 获取科室挂号占比
     */
    List<Map<String, Object>> getDepartmentRatio();
}
