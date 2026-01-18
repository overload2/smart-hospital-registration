package com.hospital.registration.controller;

import com.hospital.registration.common.Result;
import com.hospital.registration.entity.Department;
import com.hospital.registration.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: DepartmentController
 * @author: Su
 * @date: 2026/1/12 18:40
 * @version: 1.0
 * @description: 科室控制器
 */
@Slf4j
@RestController
@RequestMapping("/department")
public class DepartmentController {

    private final DepartmentService departmentService;

    // 构造器注入
    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 查询所有科室列表
     * GET /api/hospital/department/list
     */
    @GetMapping("/list")
    public Result getDepartmentList() {
        log.info("收到查询科室列表请求");

        List<Department> departments = departmentService.getAllDepartments();

        return Result.ok().data("departments", departments);
    }

    /**
     * 查询科室详情
     * GET /api/hospital/department/{id}
     */
    @GetMapping("/{id}")
    public Result getDepartmentById(@PathVariable Long id) {
        log.info("收到查询科室详情请求 - ID: {}", id);

        Department department = departmentService.getDepartmentById(id);

        return Result.ok().data("department", department);
    }
}

