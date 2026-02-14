package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.ScheduleDTO;
import com.hospital.registration.service.ScheduleService;
import com.hospital.registration.vo.ScheduleVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @title: ScheduleController
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 排班管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/schedule")
public class ScheduleController {

    private final ScheduleService scheduleService;

    public ScheduleController(ScheduleService scheduleService) {
        this.scheduleService = scheduleService;
    }

    /**
     * 新增排班
     */
    @PostMapping("/add")
    public Result addSchedule(@Valid @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleVO scheduleVO = scheduleService.addSchedule(scheduleDTO);
        return Result.ok("排班新增成功").data("schedule", scheduleVO);
    }

    /**
     * 批量新增排班
     */
    @PostMapping("/batch")
    public Result batchAddSchedules(@RequestBody List<@Valid ScheduleDTO> scheduleDTOList) {
        List<ScheduleVO> scheduleVOList = scheduleService.batchAddSchedules(scheduleDTOList);
        return Result.ok("批量排班创建完成").data("schedules", scheduleVOList);
    }

    /**
     * 更新排班信息
     */
    @PostMapping("/{id}")
    public Result updateSchedule(@PathVariable Long id,
                                 @Valid @RequestBody ScheduleDTO scheduleDTO) {
        ScheduleVO scheduleVO = scheduleService.updateSchedule(id, scheduleDTO);
        return Result.ok("排班更新成功").data("schedule", scheduleVO);
    }

    /**
     * 删除排班
     */
    @PostMapping("/delete/{id}")
    public Result deleteSchedule(@PathVariable Long id) {
        scheduleService.deleteSchedule(id);
        return Result.ok("排班删除成功");
    }

    /**
     * 根据ID查询排班详情
     */
    @GetMapping("/{id}")
    public Result getScheduleById(@PathVariable Long id) {
        ScheduleVO scheduleVO = scheduleService.getScheduleById(id);
        return Result.ok().data("schedule", scheduleVO);
    }

    /**
     * 分页查询排班列表
     */
    @GetMapping("/page")
    public Result getSchedulePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate scheduleDate,
            @RequestParam(required = false) Integer status) {
        Page<ScheduleVO> page = scheduleService.getSchedulePage(
                pageNum, pageSize, doctorId, departmentId, scheduleDate, status);
        return Result.ok().data("page", page);
    }

    /**
     * 查询指定医生的可预约排班列表
     */
    @GetMapping("/doctor/{doctorId}/available")
    public Result getAvailableSchedulesByDoctor(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<ScheduleVO> schedules = scheduleService.getAvailableSchedulesByDoctor(doctorId, startDate, endDate);
        return Result.ok().data("schedules", schedules);
    }

    /**
     * 查询指定科室的可预约排班列表
     */
    @GetMapping("/department/{departmentId}/available")
    public Result getAvailableSchedulesByDepartment(
            @PathVariable Long departmentId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate) {
        List<ScheduleVO> schedules = scheduleService.getAvailableSchedulesByDepartment(departmentId, startDate, endDate);
        return Result.ok().data("schedules", schedules);
    }

    /**
     * 更新排班状态
     */
    @PostMapping("/{id}/status")
    public Result updateScheduleStatus(@PathVariable Long id,
                                       @RequestParam Integer status) {
        scheduleService.updateScheduleStatus(id, status);
        return Result.ok("排班状态更新成功");
    }
}
