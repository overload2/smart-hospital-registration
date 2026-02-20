package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.MedicalRecord;
import com.hospital.registration.vo.MedicalRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author suzd
* @description 针对表【medical_record(就诊记录表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.MedicalRecord
*/
@Mapper
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {

    /**
     * 根据挂号ID查询病历
     */
    MedicalRecord selectByRegistrationId(@Param("registrationId") Long registrationId);

    /**
     * 查询病历详情（包含关联信息）
     */
    MedicalRecordVO selectDetailById(@Param("id") Long id);

    /**
     * 查询患者的病历列表
     */
    List<MedicalRecordVO> selectByPatientId(@Param("patientId") Long patientId);

    /**
     * 查询医生的病历列表
     */
    List<MedicalRecordVO> selectByDoctorId(@Param("doctorId") Long doctorId);

    /**
     * 分页查询医生历史病历
     */
    Page<MedicalRecordVO> selectDoctorHistoryPage(Page<MedicalRecordVO> page,
                                                  @Param("doctorId") Long doctorId,
                                                  @Param("patientName") String patientName,
                                                  @Param("diagnosis") String diagnosis,
                                                  @Param("startDate") String startDate,
                                                  @Param("endDate") String endDate);

}
