package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.DoctorEvaluation;
import org.apache.ibatis.annotations.Mapper;

/**
* @author suzd
* @description 针对表【doctor_evaluation(医生评价表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.DoctorEvaluation
*/
@Mapper
public interface DoctorEvaluationMapper extends BaseMapper<DoctorEvaluation> {

    int deleteByPrimaryKey(Long id);

    int insert(DoctorEvaluation record);

    int insertSelective(DoctorEvaluation record);

    DoctorEvaluation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(DoctorEvaluation record);

    int updateByPrimaryKey(DoctorEvaluation record);

}
