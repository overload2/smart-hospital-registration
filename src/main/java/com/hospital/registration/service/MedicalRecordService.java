package com.hospital.registration.service;

import com.hospital.registration.dto.MedicalRecordDTO;
import com.hospital.registration.vo.MedicalRecordVO;

import java.util.List;

/**
 * @title: MedicalRecordService
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 病历服务接口
 */
public interface MedicalRecordService {

    /**
     * 创建病历
     * @param medicalRecordDTO 病历信息
     * @param doctorId 医生ID
     * @return 病历VO
     */
    MedicalRecordVO createMedicalRecord(MedicalRecordDTO medicalRecordDTO, Long doctorId);

    /**
     * 修改病历
     * @param id 病历ID
     * @param medicalRecordDTO 病历信息
     * @param doctorId 医生ID
     * @return 病历VO
     */
    MedicalRecordVO updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO, Long doctorId);

    /**
     * 根据ID查询病历详情
     * @param id 病历ID
     * @return 病历VO
     */
    MedicalRecordVO getMedicalRecordById(Long id);

    /**
     * 根据挂号ID查询病历
     * @param registrationId 挂号ID
     * @return 病历VO
     */
    MedicalRecordVO getMedicalRecordByRegistrationId(Long registrationId);

    /**
     * 查询患者的病历列表
     * @param patientId 患者ID
     * @return 病历列表
     */
    List<MedicalRecordVO> getPatientMedicalRecords(Long patientId);

    /**
     * 查询医生的病历列表
     * @param doctorId 医生ID
     * @return 病历列表
     */
    List<MedicalRecordVO> getDoctorMedicalRecords(Long doctorId);
}