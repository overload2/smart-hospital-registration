package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.MedicalRecordDTO;
import com.hospital.registration.dto.MedicalRecordQueryDTO;
import com.hospital.registration.service.MedicalRecordService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.vo.MedicalRecordVO;
import com.hospital.registration.vo.RegistrationVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: MedicalRecordController
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 病历控制器
 */
@Slf4j
@RestController
@RequestMapping("/medical-record")
public class MedicalRecordController {

    private final MedicalRecordService medicalRecordService;
    private final JwtUtil jwtUtil;

    public MedicalRecordController(MedicalRecordService medicalRecordService, JwtUtil jwtUtil) {
        this.medicalRecordService = medicalRecordService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * 创建病历（医生）
     */
    @PostMapping
    @RequirePermission("medical:add")
    @OperationLog(module = "病历管理", operation = "ADD")
    public Result createMedicalRecord(@Validated @RequestBody MedicalRecordDTO medicalRecordDTO,
                                      @RequestHeader("Authorization") String authHeader) {
        // 从 token 中获取医生ID
        String token = authHeader.replace("Bearer ", "");
        Long doctorId = jwtUtil.getUserIdFromToken(token);
        if (doctorId == null) {
            return Result.error("无效的token");
        }

        log.info("创建病历 - 挂号ID: {}, 医生ID: {}", medicalRecordDTO.getRegistrationId(), doctorId);
        MedicalRecordVO medicalRecordVO = medicalRecordService.createMedicalRecord(medicalRecordDTO, doctorId);
        return Result.ok("病历创建成功").data("medicalRecord", medicalRecordVO);
    }

    /**
     * 修改病历（医生）
     */
    @PostMapping("/{id}")
    @RequirePermission("medical:edit")
    @OperationLog(module = "病历管理", operation = "UPDATE")
    public Result updateMedicalRecord(@PathVariable Long id,
                                      @Validated @RequestBody MedicalRecordDTO medicalRecordDTO,
                                      @RequestHeader("Authorization") String authHeader) {
        // 从 token 中获取医生ID
        String token = authHeader.replace("Bearer ", "");
        Long doctorId = jwtUtil.getUserIdFromToken(token);
        if (doctorId == null) {
            return Result.error("无效的token");
        }

        log.info("修改病历 - ID: {}, 医生ID: {}", id, doctorId);
        MedicalRecordVO medicalRecordVO = medicalRecordService.updateMedicalRecord(id, medicalRecordDTO, doctorId);
        return Result.ok("病历修改成功").data("medicalRecord", medicalRecordVO);
    }

    /**
     * 查询病历详情
     */
    @GetMapping("/{id}")
    public Result getMedicalRecordById(@PathVariable Long id) {
        log.info("查询病历详情 - ID: {}", id);
        MedicalRecordVO medicalRecordVO = medicalRecordService.getMedicalRecordById(id);
        return Result.ok().data("medicalRecord", medicalRecordVO);
    }

    /**
     * 根据挂号ID查询病历
     */
    @GetMapping("/registration/{registrationId}")
    public Result getMedicalRecordByRegistrationId(@PathVariable Long registrationId) {
        log.info("根据挂号ID查询病历 - 挂号ID: {}", registrationId);
        MedicalRecordVO medicalRecordVO = medicalRecordService.getMedicalRecordByRegistrationId(registrationId);
        return Result.ok().data("medicalRecord", medicalRecordVO);
    }

    /**
     * 查询我的病历（患者）
     */
    @GetMapping("/my")
    public Result getMyMedicalRecords(@RequestHeader("Authorization") String authHeader) {
        // 从 token 中获取患者ID
        String token = authHeader.replace("Bearer ", "");
        Long patientId = jwtUtil.getUserIdFromToken(token);
        if (patientId == null) {
            return Result.error("无效的token");
        }

        log.info("查询我的病历 - 患者ID: {}", patientId);
        List<MedicalRecordVO> records = medicalRecordService.getPatientMedicalRecords(patientId);
        return Result.ok().data("records", records);
    }

    /**
     * 查询医生的病历列表（医生）
     */
    @GetMapping("/doctor")
    public Result getDoctorMedicalRecords(@RequestHeader("Authorization") String authHeader) {
        // 从 token 中获取医生ID
        String token = authHeader.replace("Bearer ", "");
        Long doctorId = jwtUtil.getUserIdFromToken(token);
        if (doctorId == null) {
            return Result.error("无效的token");
        }

        log.info("查询医生的病历列表 - 医生ID: {}", doctorId);
        List<MedicalRecordVO> records = medicalRecordService.getDoctorMedicalRecords(doctorId);
        return Result.ok().data("records", records);
    }

    /**
     * 获取医生今日待诊列表
     */
    @GetMapping("/today-patients")
    public Result getTodayPatients(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取医生今日待诊列表 - 用户ID: {}", userId);
        List<RegistrationVO> registrations = medicalRecordService.getDoctorTodayPatients(userId);
        return Result.ok().data("registrations", registrations);
    }

    /**
     * 开始接诊（更新挂号状态为就诊中）
     */
    @PostMapping("/start-consultation/{registrationId}")
    @OperationLog(module = "病历管理", operation = "UPDATE")
    public Result startConsultation(@PathVariable Long registrationId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("开始接诊 - 挂号ID: {}, 用户ID: {}", registrationId, userId);
        medicalRecordService.startConsultation(registrationId, userId);
        return Result.ok("开始接诊");
    }

    /**
     * 完成接诊（保存病历并更新挂号状态为已完成）
     */
    @PostMapping("/complete-consultation")
    @OperationLog(module = "病历管理", operation = "ADD")
    public Result completeConsultation(@RequestBody MedicalRecordDTO medicalRecordDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("完成接诊 - 挂号ID: {}, 用户ID: {}", medicalRecordDTO.getRegistrationId(), userId);
        MedicalRecordVO medicalRecordVO = medicalRecordService.completeConsultation(medicalRecordDTO, userId);
        return Result.ok("接诊完成").data("medicalRecord", medicalRecordVO);
    }

    /**
     * 分页查询医生历史病历
     */
    @PostMapping("/history/page")
    public Result getHistoryPage(@RequestBody MedicalRecordQueryDTO queryDTO, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("分页查询医生历史病历 - 用户ID: {}", userId);
        Page<MedicalRecordVO> page = medicalRecordService.getDoctorHistoryPage(queryDTO, userId);
        return Result.ok().data("page", page);
    }

    /**
     * 查询患者历史病历（接诊时查看）
     */
    @GetMapping("/patient-history/{patientId}")
    public Result getPatientHistory(@PathVariable Long patientId) {
        log.info("查询患者历史病历 - 患者ID: {}", patientId);
        List<MedicalRecordVO> records = medicalRecordService.getPatientMedicalRecords(patientId);
        return Result.ok().data("records", records);
    }
}
