package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.RegistrationDTO;
import com.hospital.registration.vo.RegistrationVO;

import java.time.LocalDate;
import java.util.List;

/**
 * @title: RegistrationService
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 挂号服务接口
 */
public interface RegistrationService {

    /**
     * 创建挂号
     */
    RegistrationVO createRegistration(RegistrationDTO registrationDTO);

    /**
     * 取消挂号
     */
    void cancelRegistration(Long id);

    /**
     * 根据ID查询挂号详情
     */
    RegistrationVO getRegistrationById(Long id);

    /**
     * 根据挂号单号查询挂号详情
     */
    RegistrationVO getRegistrationByNo(String registrationNo);

    /**
     * 分页查询挂号列表
     */
    Page<RegistrationVO> getRegistrationPage(Integer pageNum, Integer pageSize,
                                             Long patientId, Long doctorId,
                                             Long departmentId, LocalDate registrationDate,
                                             String status);

    /**
     * 查询患者的挂号记录列表
     */
    List<RegistrationVO> getPatientRegistrations(Long patientId);

    /**
     * 查询医生指定日期的挂号记录列表
     */
    List<RegistrationVO> getDoctorRegistrations(Long doctorId, LocalDate registrationDate);

    /**
     * 更新挂号状态
     */
    void updateRegistrationStatus(Long id, String status);

    /**
     * 确认支付
     */
    void confirmPayment(Long id);
}

