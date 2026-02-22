package com.hospital.registration.service.impl;

import com.hospital.registration.mapper.DoctorMapper;
import com.hospital.registration.mapper.MedicalRecordMapper;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.service.DashboardService;
import com.hospital.registration.vo.DashboardStatsVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * @title: DashboardServiceImpl
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 首页仪表盘服务实现类
 */
@Slf4j
@Service
public class DashboardServiceImpl implements DashboardService {

    private final RegistrationMapper registrationMapper;
    private final MedicalRecordMapper medicalRecordMapper;
    private final DoctorMapper doctorMapper;

    public DashboardServiceImpl(RegistrationMapper registrationMapper,
                                MedicalRecordMapper medicalRecordMapper,
                                DoctorMapper doctorMapper) {
        this.registrationMapper = registrationMapper;
        this.medicalRecordMapper = medicalRecordMapper;
        this.doctorMapper = doctorMapper;
    }

    @Override
    public DashboardStatsVO getStats() {
        DashboardStatsVO stats = new DashboardStatsVO();
        // 今日预约数
        stats.setTodayAppointments(registrationMapper.countTodayRegistrations());
        // 待就诊数（PENDING + CALLED）
        stats.setPendingCount(registrationMapper.countPendingRegistrations());
        // 累计患者数（有就诊记录的患者）
        stats.setTotalPatients(medicalRecordMapper.countDistinctPatients());
        // 医生总数
        stats.setTotalDoctors(doctorMapper.countDoctors());
        return stats;
    }

    @Override
    public List<Map<String, Object>> getRegistrationTrend() {
        return registrationMapper.selectRegistrationTrend();
    }

    @Override
    public List<Map<String, Object>> getDepartmentRatio() {
        return registrationMapper.selectDepartmentRatio();
    }
}
