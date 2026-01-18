package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.Registration;
import org.apache.ibatis.annotations.Mapper;

/**
* @author suzd
* @description 针对表【registration(挂号记录表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.Registration
*/
@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {

    int deleteByPrimaryKey(Long id);

    int insert(Registration record);

    int insertSelective(Registration record);

    Registration selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Registration record);

    int updateByPrimaryKey(Registration record);

}
