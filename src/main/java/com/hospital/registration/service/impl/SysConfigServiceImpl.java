package com.hospital.registration.service.impl;

import com.hospital.registration.dto.SysConfigDTO;
import com.hospital.registration.entity.SysConfig;
import com.hospital.registration.mapper.SysConfigMapper;
import com.hospital.registration.service.SysConfigService;
import com.hospital.registration.vo.SysConfigVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @title 系统配置Service实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置业务实现
 */
@Service
public class SysConfigServiceImpl implements SysConfigService {

    @Autowired
    private SysConfigMapper sysConfigMapper;

    /**
     * 获取所有配置
     *
     * @return 配置VO列表
     */
    @Override
    public List<SysConfigVO> getAllConfigs() {
        List<SysConfig> configs = sysConfigMapper.selectAllConfigs();
        List<SysConfigVO> voList = new ArrayList<>();
        for (SysConfig config : configs) {
            SysConfigVO vo = new SysConfigVO();
            BeanUtils.copyProperties(config, vo);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 根据配置键获取配置值
     *
     * @param configKey 配置键
     * @return 配置值，不存在返回null
     */
    @Override
    public String getConfigValue(String configKey) {
        SysConfig config = sysConfigMapper.selectByConfigKey(configKey);
        if (config == null) {
            return null;
        }
        return config.getConfigValue();
    }

    /**
     * 根据配置键获取数值类型配置
     *
     * @param configKey 配置键
     * @return 配置值(Integer类型)，不存在返回null
     */
    @Override
    public Integer getConfigValueAsInt(String configKey) {
        String value = getConfigValue(configKey);
        if (value == null) {
            return null;
        }
        return Integer.parseInt(value);
    }

    /**
     * 新增配置
     *
     * @param dto 配置信息DTO
     * @throws RuntimeException 当配置键已存在时抛出异常
     */
    @Override
    public void addConfig(SysConfigDTO dto) {
        // 检查配置键是否已存在
        SysConfig existing = sysConfigMapper.selectByConfigKey(dto.getConfigKey());
        if (existing != null) {
            throw new RuntimeException("配置键已存在");
        }
        SysConfig config = new SysConfig();
        BeanUtils.copyProperties(dto, config);
        if (config.getConfigType() == null) {
            config.setConfigType("STRING");
        }
        sysConfigMapper.insert(config);
    }

    /**
     * 修改配置
     *
     * @param dto 配置信息DTO
     * @throws RuntimeException 当配置ID为空、配置不存在或配置键冲突时抛出异常
     */
    @Override
    public void updateConfig(SysConfigDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("配置ID不能为空");
        }
        SysConfig config = sysConfigMapper.selectById(dto.getId());
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }
        // 检查配置键是否与其他记录冲突
        SysConfig existing = sysConfigMapper.selectByConfigKey(dto.getConfigKey());
        if (existing != null && !existing.getId().equals(dto.getId())) {
            throw new RuntimeException("配置键已存在");
        }
        BeanUtils.copyProperties(dto, config);
        sysConfigMapper.updateById(config);
    }

    /**
     * 删除配置
     *
     * @param id 配置ID
     * @throws RuntimeException 当配置不存在时抛出异常
     */
    @Override
    public void deleteConfig(Long id) {
        SysConfig config = sysConfigMapper.selectById(id);
        if (config == null) {
            throw new RuntimeException("配置不存在");
        }
        sysConfigMapper.deleteById(id);
    }
}
