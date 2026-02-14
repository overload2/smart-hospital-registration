package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.MedicalRecord;
import org.apache.ibatis.annotations.Mapper;

/**
* @author suzd
* @description 针对表【medical_record(就诊记录表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.MedicalRecord
*/
@Mapper
public interface MedicalRecordMapper extends BaseMapper<MedicalRecord> {

    int deleteByPrimaryKey(Long id);

    int insert(MedicalRecord record);

    int insertSelective(MedicalRecord record);

    MedicalRecord selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MedicalRecord record);

    int updateByPrimaryKey(MedicalRecord record);

}
