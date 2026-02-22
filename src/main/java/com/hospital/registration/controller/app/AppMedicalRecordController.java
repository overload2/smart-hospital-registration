package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.MedicalRecordService;
import com.hospital.registration.vo.MedicalRecordVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: AppMedicalRecordController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端病历控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/medical-record")
public class AppMedicalRecordController {

    private final MedicalRecordService medicalRecordService;

    /**
     * 构造器注入
     */
    public AppMedicalRecordController(MedicalRecordService medicalRecordService) {
        this.medicalRecordService = medicalRecordService;
    }

    /**
     * 获取我的病历列表
     */
    @GetMapping("/my-list")
    public Result myList(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端获取病历列表 - 用户ID: {}", userId);
        List<MedicalRecordVO> records = medicalRecordService.getPatientMedicalRecords(userId);
        return Result.ok().data("records", records);
    }

    /**
     * 获取病历详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端获取病历详情 - ID: {}, 用户ID: {}", id, userId);

        MedicalRecordVO record = medicalRecordService.getMedicalRecordById(id);
        if (!record.getPatientId().equals(userId)) {
            return Result.error("无权查看该病历");
        }
        return Result.ok().data("record", record);
    }

    /**
     * 根据挂号ID获取病历
     */
    @GetMapping("/registration/{registrationId}")
    public Result getByRegistration(@PathVariable Long registrationId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("根据挂号ID获取病历 - 挂号ID: {}, 用户ID: {}", registrationId, userId);

        MedicalRecordVO record = medicalRecordService.getMedicalRecordByRegistrationId(registrationId);
        if (record == null) {
            return Result.ok().data("record", null);
        }
        if (!record.getPatientId().equals(userId)) {
            return Result.error("无权查看该病历");
        }
        return Result.ok().data("record", record);
    }
}