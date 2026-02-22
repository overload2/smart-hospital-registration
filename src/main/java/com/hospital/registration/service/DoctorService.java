package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.DoctorDTO;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.vo.DoctorVO;

import java.util.List;

/**
 * @title: DoctorService
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 医生服务接口
 */
public interface DoctorService {

    /**
     * 新增医生
     */
    DoctorVO addDoctor(DoctorDTO doctorDTO);

    /**
     * 更新医生信息
     */
    DoctorVO updateDoctor(Long id, DoctorDTO doctorDTO);

    /**
     * 删除医生
     */
    void deleteDoctor(Long id);

    /**
     * 根据ID查询医生详情
     */
    DoctorVO getDoctorById(Long id);

    /**
     * 分页查询医生列表
     */
    Page<DoctorVO> getDoctorPage(Integer pageNum, Integer pageSize,
                                 String keyword, Long departmentId, Integer status);

    /**
     * 查询指定科室的所有启用医生
     */
    List<DoctorVO> getActiveDoctorsByDepartment(Long departmentId);

    /**
     * 更新医生状态
     */
    void updateDoctorStatus(Long id, Integer status);

    /**
     * 批量更新医生状态
     * @param ids 医生ID列表
     * @param status 状态
     */
    void batchUpdateStatus(List<Long> ids, Integer status);

    /**
     * 根据科室获取未来7天有排班的医生列表（患者端）
     */
    List<DoctorVO> getDoctorsWithScheduleByDepartment(Long departmentId);
    /**
     * 根据用户ID查询医生信息
     * @param userId 用户ID
     * @return 医生信息
     */
    Doctor selectByUserId(Long userId);
}
