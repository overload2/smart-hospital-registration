package com.hospital.registration.controller;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.MedicalRecordDTO;
import com.hospital.registration.service.MedicalRecordService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.vo.MedicalRecordVO;
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
    @PutMapping("/{id}")
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
}
