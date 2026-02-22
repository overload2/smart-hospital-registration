package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.RegistrationDTO;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.service.DoctorService;
import com.hospital.registration.service.RegistrationService;
import com.hospital.registration.vo.RegistrationVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

/**
 * @title: RegistrationController
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 挂号管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/registration")
public class RegistrationController {

    private final RegistrationService registrationService;
    private final DoctorService doctorService;

    public RegistrationController(RegistrationService registrationService, DoctorService doctorService) {
        this.registrationService = registrationService;
        this.doctorService = doctorService;
    }

    /**
     * 创建挂号
     */
    @PostMapping("/create")
    @RequirePermission("registration:create")
    @OperationLog(module = "挂号管理", operation = "ADD")
    public Result createRegistration(@Valid @RequestBody RegistrationDTO registrationDTO) {
        RegistrationVO registrationVO = registrationService.createRegistration(registrationDTO);
        return Result.ok("挂号成功").data("registration", registrationVO);
    }

    /**
     * 取消挂号
     */
    @PostMapping("/cancel/{id}")
    @RequirePermission("registration:cancel")
    @OperationLog(module = "挂号管理", operation = "UPDATE")
    public Result cancelRegistration(@PathVariable Long id) {
        registrationService.cancelRegistration(id);
        return Result.ok("挂号取消成功");
    }

    /**
     * 根据ID查询挂号详情
     */
    @GetMapping("/{id}")
    public Result getRegistrationById(@PathVariable Long id) {
        RegistrationVO registrationVO = registrationService.getRegistrationById(id);
        return Result.ok().data("registration", registrationVO);
    }

    /**
     * 根据挂号单号查询挂号详情
     */
    @GetMapping("/no/{registrationNo}")
    public Result getRegistrationByNo(@PathVariable String registrationNo) {
        RegistrationVO registrationVO = registrationService.getRegistrationByNo(registrationNo);
        return Result.ok().data("registration", registrationVO);
    }


    /**
     * 查询患者的挂号记录列表
     */
    @GetMapping("/patient/{patientId}")
    public Result getPatientRegistrations(@PathVariable Long patientId) {
        List<RegistrationVO> registrations = registrationService.getPatientRegistrations(patientId);
        return Result.ok().data("registrations", registrations);
    }

    /**
     * 查询医生指定日期的挂号记录列表
     */
    @GetMapping("/doctor/{doctorId}")
    public Result getDoctorRegistrations(
            @PathVariable Long doctorId,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate registrationDate) {
        List<RegistrationVO> registrations = registrationService.getDoctorRegistrations(
                doctorId, registrationDate);
        return Result.ok().data("registrations", registrations);
    }

    /**
     * 更新挂号状态
     */
    @PostMapping("/{id}/status")
    @OperationLog(module = "挂号管理", operation = "UPDATE")
    public Result updateRegistrationStatus(@PathVariable Long id,
                                           @RequestParam String status) {
        registrationService.updateRegistrationStatus(id, status);
        return Result.ok("挂号状态更新成功");
    }

    /**
     * 分页查询挂号列表（增强版）
     */
    @GetMapping("/page")
    public Result getRegistrationPage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long patientId,
            @RequestParam(required = false) Long doctorId,
            @RequestParam(required = false) Long departmentId,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate registrationDate,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String registrationNo,
            @RequestParam(required = false) String patientName,
            @RequestParam(required = false) String patientPhone,
            @RequestParam(required = false) String patientIdCard,
            HttpServletRequest request) {

        // 获取当前登录用户信息
        Long currentUserId = (Long) request.getAttribute("userId");
        String currentUserRole = (String) request.getAttribute("userRole");

        // 如果是医生角色，只能查看自己的挂号
        if ("DOCTOR".equals(currentUserRole)) {
            // 根据userId获取对应的doctorId
            Doctor doctor = doctorService.selectByUserId(currentUserId);
            if(doctor != null){
                doctorId = doctor.getId();
            }

        }

        Page<RegistrationVO> page = registrationService.getRegistrationPage(
                pageNum, pageSize, patientId, doctorId, departmentId, registrationDate,
                status, registrationNo, patientName, patientPhone, patientIdCard);
        return Result.ok().data("page", page);
    }

    /**
     * 叫号
     */
    @PostMapping("/{id}/call")
    @OperationLog(module = "挂号管理", operation = "UPDATE")
    public Result callNumber(@PathVariable Long id) {
        RegistrationVO vo = registrationService.callNumber(id);
        return Result.ok("叫号成功").data("registration", vo);
    }

    /**
     * 过号 - 重新排队
     */
    @PostMapping("/{id}/missed/requeue")
    @OperationLog(module = "挂号管理", operation = "UPDATE")
    public Result missedRequeue(@PathVariable Long id) {
        registrationService.missedRequeue(id);
        return Result.ok("已重新排队");
    }

    /**
     * 过号 - 标记爽约
     */
    @PostMapping("/{id}/missed/noshow")
    @OperationLog(module = "挂号管理", operation = "UPDATE")
    public Result missedNoShow(@PathVariable Long id) {
        registrationService.missedNoShow(id);
        return Result.ok("已标记爽约");
    }

    /**
     * 获取今日候诊队列
     */
    @GetMapping("/queue/today")
    public Result getTodayQueue(@RequestParam Long doctorId) {
        List<RegistrationVO> queue = registrationService.getTodayQueue(doctorId);
        return Result.ok().data("queue", queue);
    }

    /**
     * 获取当前叫号信息
     */
    @GetMapping("/queue/current")
    public Result getCurrentCalled(@RequestParam Long doctorId) {
        RegistrationVO vo = registrationService.getCurrentCalled(doctorId);
        return Result.ok().data("current", vo);
    }

//    /**
//     * 确认支付
//     */
//    @PostMapping("/{id}/pay")
//    public Result confirmPayment(@PathVariable Long id) {
//        registrationService.confirmPayment(id);
//        return Result.ok("支付成功");
//    }
}

