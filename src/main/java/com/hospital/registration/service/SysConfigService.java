package com.hospital.registration.service;

import com.hospital.registration.dto.SysConfigDTO;
import com.hospital.registration.vo.SysConfigVO;

import java.util.List;

/**
 * @title 系统配置Service
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置业务接口
 */
public interface SysConfigService {

    // 获取所有配置
    List<SysConfigVO> getAllConfigs();

    // 根据配置键获取配置值
    String getConfigValue(String configKey);

    // 根据配置键获取数值类型配置
    Integer getConfigValueAsInt(String configKey);

    // 新增配置
    void addConfig(SysConfigDTO dto);

    // 修改配置
    void updateConfig(SysConfigDTO dto);

    // 删除配置
    void deleteConfig(Long id);
}
