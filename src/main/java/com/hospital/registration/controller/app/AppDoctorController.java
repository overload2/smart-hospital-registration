package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.DoctorService;
import com.hospital.registration.vo.DoctorVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: AppDoctorController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端医生控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/doctor")
public class AppDoctorController {

    private final DoctorService doctorService;

    /**
     * 构造器注入
     */
    public AppDoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * 根据科室获取有排班的医生列表
     */
    @GetMapping("/list")
    public Result listByDepartment(@RequestParam Long departmentId) {
        log.info("患者端获取医生列表 - 科室ID: {}", departmentId);
        List<DoctorVO> doctors = doctorService.getDoctorsWithScheduleByDepartment(departmentId);
        return Result.ok().data("doctors", doctors);
    }

    /**
     * 获取医生详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        log.info("患者端获取医生详情 - ID: {}", id);
        DoctorVO doctor = doctorService.getDoctorById(id);
        return Result.ok().data("doctor", doctor);
    }
}
