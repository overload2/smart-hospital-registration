package com.hospital.registration.controller;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.DashboardService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @title: DashboardController
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 首页仪表盘控制器
 */
@Slf4j
@RestController
@RequestMapping("/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    /**
     * 获取首页统计数据
     */
    @GetMapping("/stats")
    public Result getStats() {
        log.info("获取首页统计数据");
        return Result.ok().data("stats", dashboardService.getStats());
    }

    /**
     * 获取近7天挂号趋势
     */
    @GetMapping("/registration-trend")
    public Result getRegistrationTrend() {
        log.info("获取近7天挂号趋势");
        return Result.ok().data("list", dashboardService.getRegistrationTrend());
    }

    /**
     * 获取科室挂号占比
     */
    @GetMapping("/department-ratio")
    public Result getDepartmentRatio() {
        log.info("获取科室挂号占比");
        return Result.ok().data("list", dashboardService.getDepartmentRatio());
    }
}
