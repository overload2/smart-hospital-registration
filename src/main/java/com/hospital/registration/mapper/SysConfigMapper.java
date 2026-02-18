package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.SysConfig;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title 系统配置Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置数据访问接口
 */
@Mapper
public interface SysConfigMapper extends BaseMapper<SysConfig> {

    // 根据配置键查询
    SysConfig selectByConfigKey(@Param("configKey") String configKey);

    // 查询所有配置
    List<SysConfig> selectAllConfigs();
}

