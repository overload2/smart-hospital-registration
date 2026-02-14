package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.DepartmentDTO;
import com.hospital.registration.service.DepartmentService;
import com.hospital.registration.vo.DepartmentVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

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

    public DepartmentController(DepartmentService departmentService) {
        this.departmentService = departmentService;
    }

    /**
     * 新增科室
     */
    @PostMapping("/add")
    public Result addDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) {
        DepartmentVO departmentVO = departmentService.addDepartment(departmentDTO);
        return Result.ok("科室新增成功").data("department", departmentVO);
    }

    /**
     * 更新科室信息
     */
    @PostMapping("/{id}")
    public Result updateDepartment(@PathVariable Long id,
                                   @Valid @RequestBody DepartmentDTO departmentDTO) {
        DepartmentVO departmentVO = departmentService.updateDepartment(id, departmentDTO);
        return Result.ok("科室更新成功").data("department", departmentVO);
    }

    /**
     * 删除科室
     */
    @PostMapping("/delete/{id}")
    public Result deleteDepartment(@PathVariable Long id) {
        departmentService.deleteDepartment(id);
        return Result.ok("科室删除成功");
    }

    /**
     * 根据ID查询科室
     */
    @GetMapping("/{id}")
    public Result getDepartmentById(@PathVariable Long id) {
        DepartmentVO departmentVO = departmentService.getDepartmentById(id);
        return Result.ok().data("department", departmentVO);
    }

    /**
     * 分页查询科室列表
     */
    @GetMapping("/page")
    public Result getDepartmentPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword) {
        Page<DepartmentVO> page = departmentService.getDepartmentPage(pageNum, pageSize, keyword);
        return Result.ok().data("page", page);
    }

    /**
     * 获取所有启用的科室列表
     */
    @GetMapping("/active")
    public Result getActiveDepartments() {
        List<DepartmentVO> departments = departmentService.getActiveDepartments();
        return Result.ok().data("departments", departments);
    }

    /**
     * 更新科室状态
     */
    @PostMapping("/{id}/status")
    public Result updateDepartmentStatus(@PathVariable Long id,
                                         @RequestParam Integer status) {
        departmentService.updateDepartmentStatus(id, status);
        return Result.ok("科室状态更新成功");
    }
}

