package com.hospital.registration.service.impl;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.MedicalRecordDTO;
import com.hospital.registration.entity.MedicalRecord;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.mapper.MedicalRecordMapper;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.service.MedicalRecordService;
import com.hospital.registration.vo.MedicalRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title: MedicalRecordServiceImpl
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 病历服务实现类
 */
@Slf4j
@Service
public class MedicalRecordServiceImpl implements MedicalRecordService {

    private final MedicalRecordMapper medicalRecordMapper;
    private final RegistrationMapper registrationMapper;

    public MedicalRecordServiceImpl(MedicalRecordMapper medicalRecordMapper,
                                    RegistrationMapper registrationMapper) {
        this.medicalRecordMapper = medicalRecordMapper;
        this.registrationMapper = registrationMapper;
    }

    /**
     * 创建病历
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordVO createMedicalRecord(MedicalRecordDTO medicalRecordDTO, Long doctorId) {
        log.info("创建病历 - 挂号ID: {}, 医生ID: {}", medicalRecordDTO.getRegistrationId(), doctorId);

        // 查询挂号记录
        Registration registration = registrationMapper.selectById(medicalRecordDTO.getRegistrationId());
        if (registration == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        // 检查挂号状态
        if (registration.getStatus() == RegistrationStatus.CANCELLED) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "挂号已取消，无法创建病历");
        }

        // 检查是否已存在病历
        MedicalRecord existingRecord = medicalRecordMapper.selectByRegistrationId(medicalRecordDTO.getRegistrationId());
        if (existingRecord != null) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该挂号已有病历记录");
        }

        // 创建病历记录
        MedicalRecord medicalRecord = new MedicalRecord();
        BeanUtils.copyProperties(medicalRecordDTO, medicalRecord);
        medicalRecord.setPatientId(registration.getPatientId());
        medicalRecord.setDoctorId(doctorId);
        medicalRecord.setVisitTime(LocalDateTime.now());

        int result = medicalRecordMapper.insert(medicalRecord);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "病历创建失败");
        }

        // 更新挂号状态为已完成
        Registration updateRegistration = new Registration();
        updateRegistration.setId(registration.getId());
        updateRegistration.setStatus(RegistrationStatus.COMPLETED);
        registrationMapper.updateById(updateRegistration);

        log.info("病历创建成功 - ID: {}", medicalRecord.getId());

        // 查询并返回完整信息
        return medicalRecordMapper.selectDetailById(medicalRecord.getId());
    }

    /**
     * 修改病历
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordVO updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO, Long doctorId) {
        log.info("修改病历 - ID: {}, 医生ID: {}", id, doctorId);

        // 查询病历记录
        MedicalRecord medicalRecord = medicalRecordMapper.selectById(id);
        if (medicalRecord == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "病历记录不存在");
        }

        // 检查是否是创建该病历的医生
        if (!medicalRecord.getDoctorId().equals(doctorId)) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "只能修改自己创建的病历");
        }

        // 更新病历信息
        MedicalRecord updateRecord = new MedicalRecord();
        updateRecord.setId(id);
        BeanUtils.copyProperties(medicalRecordDTO, updateRecord);

        int result = medicalRecordMapper.updateById(updateRecord);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "病历修改失败");
        }

        log.info("病历修改成功 - ID: {}", id);

        // 查询并返回完整信息
        return medicalRecordMapper.selectDetailById(id);
    }

    /**
     * 根据ID查询病历详情
     */
    @Override
    public MedicalRecordVO getMedicalRecordById(Long id) {
        log.info("查询病历详情 - ID: {}", id);

        MedicalRecordVO medicalRecordVO = medicalRecordMapper.selectDetailById(id);
        if (medicalRecordVO == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "病历记录不存在");
        }

        return medicalRecordVO;
    }

    /**
     * 根据挂号ID查询病历
     */
    @Override
    public MedicalRecordVO getMedicalRecordByRegistrationId(Long registrationId) {
        log.info("根据挂号ID查询病历 - 挂号ID: {}", registrationId);

        MedicalRecord medicalRecord = medicalRecordMapper.selectByRegistrationId(registrationId);
        if (medicalRecord == null) {
            return null;
        }

        return medicalRecordMapper.selectDetailById(medicalRecord.getId());
    }

    /**
     * 查询患者的病历列表
     */
    @Override
    public List<MedicalRecordVO> getPatientMedicalRecords(Long patientId) {
        log.info("查询患者的病历列表 - 患者ID: {}", patientId);

        List<MedicalRecordVO> records = medicalRecordMapper.selectByPatientId(patientId);
        log.info("查询到 {} 条病历记录", records.size());

        return records;
    }

    /**
     * 查询医生的病历列表
     */
    @Override
    public List<MedicalRecordVO> getDoctorMedicalRecords(Long doctorId) {
        log.info("查询医生的病历列表 - 医生ID: {}", doctorId);

        List<MedicalRecordVO> records = medicalRecordMapper.selectByDoctorId(doctorId);
        log.info("查询到 {} 条病历记录", records.size());

        return records;
    }
}
