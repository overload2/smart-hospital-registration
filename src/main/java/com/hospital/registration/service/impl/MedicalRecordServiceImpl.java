package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.RegistrationStatus;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.MedicalRecordDTO;
import com.hospital.registration.dto.MedicalRecordQueryDTO;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.entity.MedicalRecord;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.mapper.DoctorMapper;
import com.hospital.registration.mapper.MedicalRecordMapper;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.service.MedicalRecordService;
import com.hospital.registration.service.PermissionService;
import com.hospital.registration.vo.MedicalRecordVO;
import com.hospital.registration.vo.RegistrationVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
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
    private final DoctorMapper doctorMapper;
    private final PermissionService permissionService;

    public MedicalRecordServiceImpl(MedicalRecordMapper medicalRecordMapper,
                                    RegistrationMapper registrationMapper,
                                    DoctorMapper doctorMapper,
                                    PermissionService permissionService) {
        this.medicalRecordMapper = medicalRecordMapper;
        this.registrationMapper = registrationMapper;
        this.doctorMapper = doctorMapper;
        this.permissionService = permissionService;
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
    public MedicalRecordVO updateMedicalRecord(Long id, MedicalRecordDTO medicalRecordDTO, Long userId) {
        log.info("修改病历 - ID: {}, 用户ID: {}", id, userId);

        // 根据userId获取doctorId
        Doctor doctor = getDoctorByUserId(userId);
        MedicalRecordVO medicalRecordVO = getMedicalRecordById(id);

        // 检查是否是创建该病历的医生
        if (!medicalRecordVO.getDoctorId().equals(doctor.getId())) {
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

    /**
     * 获取医生今日待诊列表
     */
    @Override
    public List<RegistrationVO> getDoctorTodayPatients(Long userId) {
        log.info("获取医生今日待诊列表 - 用户ID: {}", userId);

        // 判断是否是管理员
        if (permissionService.isSuperAdmin(userId)) {
            // 管理员查看所有今日待诊
            return registrationMapper.selectAllTodayPatients(LocalDate.now());
        }
        //根据userId查询doctorId
        Doctor doctor = getDoctorByUserId(userId);
        if (doctor == null) {
            log.warn("未找到对应的医生信息 - 用户ID: {}", userId);
            return new ArrayList<>();
        }
        return registrationMapper.selectDoctorTodayPatients(doctor.getId(), LocalDate.now());
    }

    /**
     * 开始接诊
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void startConsultation(Long registrationId, Long userId) {
        log.info("开始接诊 - 挂号ID: {}, 用户ID: {}", registrationId, userId);

        //根据userId查询doctorId
        Doctor doctor = getDoctorByUserId(userId);

        Registration registration = getRegistrationById(registrationId);

        validateDoctorPermission(registration, doctor.getId());

        // 验证是否是该医生的患者
        if (!registration.getDoctorId().equals(doctor.getId())) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "无权操作此挂号记录");
        }

        // 验证挂号状态（应该允许PENDING和CALLED状态）
        if (registration.getStatus() != RegistrationStatus.PENDING
                && registration.getStatus() != RegistrationStatus.CALLED) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "该患者不是待就诊状态");
        }

        // 更新状态为就诊中
        Registration updateRegistration = new Registration();
        updateRegistration.setId(registrationId);
        updateRegistration.setStatus(RegistrationStatus.CONSULTING);
        registrationMapper.updateById(updateRegistration);

        log.info("开始接诊成功 - 挂号ID: {}", registrationId);
    }

    /**
     * 完成接诊（保存病历并更新状态）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public MedicalRecordVO completeConsultation(MedicalRecordDTO medicalRecordDTO, Long userId) {
        log.info("完成接诊 - 挂号ID: {}, 用户ID: {}", medicalRecordDTO.getRegistrationId(), userId);

        // 根据userId查询doctorId
        Doctor doctor = doctorMapper.selectByUserId(userId);
        if (doctor == null) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "未找到对应的医生信息");
        }

        Registration registration = registrationMapper.selectById(medicalRecordDTO.getRegistrationId());
        if (registration == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }

        // 验证是否是该医生的患者
        if (!registration.getDoctorId().equals(doctor.getId())) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "无权操作此挂号记录");
        }

        // 检查是否已存在病历（编辑模式）
        MedicalRecord existingRecord = medicalRecordMapper.selectByRegistrationId(medicalRecordDTO.getRegistrationId());

        MedicalRecord medicalRecord;
        if (existingRecord != null) {
            // 更新病历
            medicalRecord = existingRecord;
            medicalRecord.setChiefComplaint(medicalRecordDTO.getChiefComplaint());
            medicalRecord.setPresentIllness(medicalRecordDTO.getPresentIllness());
            medicalRecord.setPastHistory(medicalRecordDTO.getPastHistory());
            medicalRecord.setAllergyHistory(medicalRecordDTO.getAllergyHistory());
            medicalRecord.setDiagnosis(medicalRecordDTO.getDiagnosis());
            medicalRecord.setPrescription(medicalRecordDTO.getPrescription());
            medicalRecord.setAdvice(medicalRecordDTO.getAdvice());
            medicalRecordMapper.updateById(medicalRecord);
            log.info("更新病历成功 - ID: {}", medicalRecord.getId());
        } else {
            // 创建新病历
            medicalRecord = new MedicalRecord();
            BeanUtils.copyProperties(medicalRecordDTO, medicalRecord);
            medicalRecord.setPatientId(registration.getPatientId());
            medicalRecord.setDoctorId(doctor.getId());
            medicalRecord.setVisitTime(LocalDateTime.now());
            medicalRecordMapper.insert(medicalRecord);
            log.info("创建病历成功 - ID: {}", medicalRecord.getId());
        }

        // 更新挂号状态为已完成
        Registration updateRegistration = new Registration();
        updateRegistration.setId(registration.getId());
        updateRegistration.setStatus(RegistrationStatus.COMPLETED);
        registrationMapper.updateById(updateRegistration);

        return medicalRecordMapper.selectDetailById(medicalRecord.getId());
    }

    /**
     * 分页查询医生历史病历
     */
    @Override
    public Page<MedicalRecordVO> getDoctorHistoryPage(MedicalRecordQueryDTO queryDTO, Long userId) {
        log.info("分页查询医生历史病历 - 用户ID: {}", userId);

        Page<MedicalRecordVO> page = new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        // 判断是否是管理员
        if (permissionService.isSuperAdmin(userId)) {
            // 管理员查看所有病历，doctorId传null
            return medicalRecordMapper.selectDoctorHistoryPage(page, null,
                    queryDTO.getPatientName(), queryDTO.getDiagnosis(),
                    queryDTO.getStartDate(), queryDTO.getEndDate());
        }
        // 根据userId获取doctorId
        Doctor doctor = doctorMapper.selectByUserId(userId);
        if (doctor == null) {
            log.warn("未找到对应的医生信息 - 用户ID: {}", userId);
            return new Page<>(queryDTO.getPageNum(), queryDTO.getPageSize());
        }

        return medicalRecordMapper.selectDoctorHistoryPage(page, doctor.getId(),
                queryDTO.getPatientName(), queryDTO.getDiagnosis(),
                queryDTO.getStartDate(), queryDTO.getEndDate());
    }

    /**
     * 根据用户ID获取医生信息
     * @param userId 用户ID
     * @return 医生实体
     * @throws BusinessException 当医生不存在时抛出异常
     */
    private Doctor getDoctorByUserId(Long userId) {
        Doctor doctor = doctorMapper.selectByUserId(userId);
        if (doctor == null) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "未找到对应的医生信息");
        }
        return doctor;
    }

    /**
     * 根据用户ID获取医生信息（允许为空）
     * @param userId 用户ID
     * @return 医生实体，可能为null
     */
    private Doctor getDoctorByUserIdOrNull(Long userId) {
        return doctorMapper.selectByUserId(userId);
    }

    /**
     * 根据ID获取挂号记录
     * @param registrationId 挂号ID
     * @return 挂号实体
     * @throws BusinessException 当挂号不存在时抛出异常
     */
    private Registration getRegistrationById(Long registrationId) {
        Registration registration = registrationMapper.selectById(registrationId);
        if (registration == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "挂号记录不存在");
        }
        return registration;
    }

    /**
     * 验证医生是否有权操作该挂号记录
     * @param registration 挂号记录
     * @param doctorId 医生ID
     * @throws BusinessException 当无权操作时抛出异常
     */
    private void validateDoctorPermission(Registration registration, Long doctorId) {
        if (!registration.getDoctorId().equals(doctorId)) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "无权操作此挂号记录");
        }
    }

}
