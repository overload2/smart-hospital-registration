package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.DepartmentService;
import com.hospital.registration.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: AppDepartmentController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端科室控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/department")
public class AppDepartmentController {

    private final DepartmentService departmentService;

    /**
     * 构造器注入
     */
    public AppDepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 获取所有启用的科室列表
     */
    @GetMapping("/list")
    public Result list() {
        log.info("患者端获取科室列表");
        List<DepartmentVO> departments = departmentService.getActiveDepartments();
        return Result.ok().data("departments", departments);
    }

    /**
     * 获取科室详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        log.info("患者端获取科室详情 - ID: {}", id);
        DepartmentVO department = departmentService.getDepartmentById(id);
        return Result.ok().data("department", department);
    }
}
