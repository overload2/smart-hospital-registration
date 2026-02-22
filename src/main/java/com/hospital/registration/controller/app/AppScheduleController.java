package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.ScheduleService;
import com.hospital.registration.vo.ScheduleVO;
import com.hospital.registration.vo.app.DetailTimeSlotVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @title: AppScheduleController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端排班控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/schedule")
public class AppScheduleController {

    private final ScheduleService scheduleService;

    /**
     * 构造器注入
     */
    public AppScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 获取医生可预约排班列表（未来7天）
     */
    @GetMapping("/available")
    public Result getAvailable(@RequestParam Long doctorId) {
        log.info("患者端获取医生可预约排班 - 医生ID: {}", doctorId);
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = startDate.plusDays(7);
        List<ScheduleVO> schedules = scheduleService.getAvailableSchedulesByDoctor(doctorId, startDate, endDate);
        return Result.ok().data("schedules", schedules);
    }

    /**
     * 获取排班详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        log.info("患者端获取排班详情 - ID: {}", id);
        ScheduleVO schedule = scheduleService.getScheduleById(id);
        return Result.ok().data("schedule", schedule);
    }
    /**
     * 获取排班的细分时段列表
     */
    @GetMapping("/{id}/detail-slots")
    public Result getDetailSlots(@PathVariable Long id, HttpServletRequest request) {
        log.info("患者端获取排班细分时段 - 排班ID: {}", id);

        Long userId = null;
        try {
            userId = (Long) request.getAttribute("userId");
        } catch (Exception e) {
            // 未登录
        }

        List<DetailTimeSlotVO> detailSlots = scheduleService.getDetailSlots(id, userId);
        return Result.ok().data("detailSlots", detailSlots);
    }
}
