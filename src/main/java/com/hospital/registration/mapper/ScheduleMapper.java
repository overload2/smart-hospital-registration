package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.Schedule;
import org.apache.ibatis.annotations.Mapper;

/**
* @author suzd
* @description 针对表【schedule(排班表)】的数据库操作Mapper
* @createDate 2026-01-18 18:54:51
* @Entity com.hospital.registration.entity.Schedule
*/
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {

    int deleteByPrimaryKey(Long id);

    int insert(Schedule record);

    int insertSelective(Schedule record);

    Schedule selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Schedule record);

    int updateByPrimaryKey(Schedule record);

}
