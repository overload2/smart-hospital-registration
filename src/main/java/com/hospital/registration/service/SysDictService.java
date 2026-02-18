package com.hospital.registration.service;

import com.hospital.registration.dto.SysDictDataDTO;
import com.hospital.registration.dto.SysDictTypeDTO;
import com.hospital.registration.vo.SysDictDataVO;
import com.hospital.registration.vo.SysDictTypeVO;

import java.util.List;

/**
 * @title 数据字典Service
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 数据字典业务接口
 */
public interface SysDictService {

    // 获取所有字典类型
    List<SysDictTypeVO> getAllDictTypes();

    // 根据字典类型获取字典数据
    List<SysDictDataVO> getDictDataByType(String dictType);

    // 根据字典类型和值获取标签
    String getDictLabel(String dictType, String dictValue);

    // 新增字典类型
    void addDictType(SysDictTypeDTO dto);

    // 修改字典类型
    void updateDictType(SysDictTypeDTO dto);

    // 删除字典类型
    void deleteDictType(Long id);

    // 新增字典数据
    void addDictData(SysDictDataDTO dto);

    // 修改字典数据
    void updateDictData(SysDictDataDTO dto);

    // 删除字典数据
    void deleteDictData(Long id);
}
