package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.DoctorDTO;
import com.hospital.registration.service.DoctorService;
import com.hospital.registration.vo.DoctorVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: DoctorController
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 医生管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/doctor")
public class DoctorController {

    private final DoctorService doctorService;

    public DoctorController(DoctorService doctorService) {
        this.doctorService = doctorService;
    }

    /**
     * 新增医生
     */
    @PostMapping("/add")
    @RequirePermission("doctor:add")
    public Result addDoctor(@Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorVO doctorVO = doctorService.addDoctor(doctorDTO);
        return Result.ok("医生新增成功").data("doctor", doctorVO);
    }

    /**
     * 更新医生信息
     */
    @PostMapping("/{id}")
    @RequirePermission("doctor:edit")
    public Result updateDoctor(@PathVariable Long id,
                               @Valid @RequestBody DoctorDTO doctorDTO) {
        DoctorVO doctorVO = doctorService.updateDoctor(id, doctorDTO);
        return Result.ok("医生更新成功").data("doctor", doctorVO);
    }

    /**
     * 删除医生
     */
    @PostMapping("/delete/{id}")
    @RequirePermission("doctor:delete")
    public Result deleteDoctor(@PathVariable Long id) {
        doctorService.deleteDoctor(id);
        return Result.ok("医生删除成功");
    }

    /**
     * 根据ID查询医生详情
     */
    @GetMapping("/{id}")
    public Result getDoctorById(@PathVariable Long id) {
        DoctorVO doctorVO = doctorService.getDoctorById(id);
        return Result.ok().data("doctor", doctorVO);
    }

    /**
     * 分页查询医生列表
     */
    @GetMapping("/page")
    public Result getDoctorPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) Integer status) {
        Page<DoctorVO> page = doctorService.getDoctorPage(pageNum, pageSize, keyword, departmentId, status);
        return Result.ok().data("page", page);
    }

    /**
     * 查询指定科室的所有启用医生
     */
    @GetMapping("/department/{departmentId}")
    public Result getActiveDoctorsByDepartment(@PathVariable Long departmentId) {
        List<DoctorVO> doctors = doctorService.getActiveDoctorsByDepartment(departmentId);
        return Result.ok().data("doctors", doctors);
    }

    /**
     * 更新医生状态
     */
    @PostMapping("/{id}/status")
    @RequirePermission("doctor:edit")
    public Result updateDoctorStatus(@PathVariable Long id,
                                     @RequestParam Integer status) {
        doctorService.updateDoctorStatus(id, status);
        return Result.ok("医生状态更新成功");
    }
}

