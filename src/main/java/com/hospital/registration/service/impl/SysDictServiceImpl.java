package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.SysDictDataDTO;
import com.hospital.registration.dto.SysDictTypeDTO;
import com.hospital.registration.entity.SysDictData;
import com.hospital.registration.entity.SysDictType;
import com.hospital.registration.mapper.SysDictDataMapper;
import com.hospital.registration.mapper.SysDictTypeMapper;
import com.hospital.registration.service.SysDictService;
import com.hospital.registration.vo.SysDictDataVO;
import com.hospital.registration.vo.SysDictTypeVO;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @title 数据字典Service实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 数据字典业务实现
 */
@Service
public class SysDictServiceImpl implements SysDictService {

    @Autowired
    private SysDictTypeMapper sysDictTypeMapper;

    @Autowired
    private SysDictDataMapper sysDictDataMapper;

    /**
     * 获取所有字典类型(包含字典数据)
     *
     * @return 字典类型VO列表，每个类型包含其下的字典数据
     */
    @Override
    public List<SysDictTypeVO> getAllDictTypes() {
        List<SysDictType> types = sysDictTypeMapper.selectAllEnabled();
        List<SysDictTypeVO> voList = new ArrayList<>();
        for (SysDictType type : types) {
            SysDictTypeVO vo = new SysDictTypeVO();
            BeanUtils.copyProperties(type, vo);
            // 查询该类型下的字典数据
            List<SysDictData> dataList = sysDictDataMapper.selectByDictType(type.getDictType());
            List<SysDictDataVO> dataVoList = new ArrayList<>();
            for (SysDictData data : dataList) {
                SysDictDataVO dataVo = new SysDictDataVO();
                BeanUtils.copyProperties(data, dataVo);
                dataVoList.add(dataVo);
            }
            vo.setDataList(dataVoList);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 根据字典类型获取字典数据列表
     *
     * @param dictType 字典类型编码
     * @return 字典数据VO列表
     */
    @Override
    public List<SysDictDataVO> getDictDataByType(String dictType) {
        List<SysDictData> dataList = sysDictDataMapper.selectByDictType(dictType);
        List<SysDictDataVO> voList = new ArrayList<>();
        for (SysDictData data : dataList) {
            SysDictDataVO vo = new SysDictDataVO();
            BeanUtils.copyProperties(data, vo);
            voList.add(vo);
        }
        return voList;
    }

    /**
     * 根据字典类型和值获取标签
     *
     * @param dictType 字典类型编码
     * @param dictValue 字典值
     * @return 字典标签，不存在返回null
     */
    @Override
    public String getDictLabel(String dictType, String dictValue) {
        SysDictData data = sysDictDataMapper.selectByTypeAndValue(dictType, dictValue);
        if (data == null) {
            return null;
        }
        return data.getDictLabel();
    }

    /**
     * 新增字典类型
     *
     * @param dto 字典类型DTO
     * @throws RuntimeException 当字典类型编码已存在时抛出异常
     */
    @Override
    public void addDictType(SysDictTypeDTO dto) {
        // 检查字典类型编码是否已存在
        SysDictType existing = sysDictTypeMapper.selectByDictType(dto.getDictType());
        if (existing != null) {
            throw new RuntimeException("字典类型编码已存在");
        }
        SysDictType dictType = new SysDictType();
        BeanUtils.copyProperties(dto, dictType);
        if (dictType.getStatus() == null) {
            dictType.setStatus(1);
        }
        sysDictTypeMapper.insert(dictType);
    }

    /**
     * 修改字典类型
     *
     * @param dto 字典类型DTO
     * @throws RuntimeException 当字典类型ID为空、不存在或编码冲突时抛出异常
     */
    @Override
    public void updateDictType(SysDictTypeDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("字典类型ID不能为空");
        }
        SysDictType dictType = sysDictTypeMapper.selectById(dto.getId());
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        // 检查字典类型编码是否与其他记录冲突
        SysDictType existing = sysDictTypeMapper.selectByDictType(dto.getDictType());
        if (existing != null && !existing.getId().equals(dto.getId())) {
            throw new RuntimeException("字典类型编码已存在");
        }
        String oldDictType = dictType.getDictType();
        BeanUtils.copyProperties(dto, dictType);
        sysDictTypeMapper.updateById(dictType);
        // 如果字典类型编码变更，需要同步更新字典数据
        if (!oldDictType.equals(dto.getDictType())) {
            List<SysDictData> dataList = sysDictDataMapper.selectByDictType(oldDictType);
            for (SysDictData data : dataList) {
                data.setDictType(dto.getDictType());
                sysDictDataMapper.updateById(data);
            }
        }
    }

    /**
     * 删除字典类型(同时删除该类型下的所有字典数据)
     *
     * @param id 字典类型ID
     * @throws RuntimeException 当字典类型不存在时抛出异常
     */
    @Override
    public void deleteDictType(Long id) {
        SysDictType dictType = sysDictTypeMapper.selectById(id);
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        // 删除字典类型下的所有数据
        sysDictDataMapper.deleteByDictType(dictType.getDictType());
        // 删除字典类型
        sysDictTypeMapper.deleteById(id);
    }

    /**
     * 新增字典数据
     *
     * @param dto 字典数据DTO
     * @throws RuntimeException 当字典类型不存在或字典值已存在时抛出异常
     */
    @Override
    public void addDictData(SysDictDataDTO dto) {
        // 检查字典类型是否存在
        SysDictType dictType = sysDictTypeMapper.selectByDictType(dto.getDictType());
        if (dictType == null) {
            throw new RuntimeException("字典类型不存在");
        }
        // 检查字典值是否已存在
        SysDictData existing = sysDictDataMapper.selectByTypeAndValue(dto.getDictType(), dto.getDictValue());
        if (existing != null) {
            throw new RuntimeException("该字典类型下字典值已存在");
        }
        SysDictData dictData = new SysDictData();
        BeanUtils.copyProperties(dto, dictData);
        if (dictData.getSortOrder() == null) {
            dictData.setSortOrder(0);
        }
        if (dictData.getStatus() == null) {
            dictData.setStatus(1);
        }
        sysDictDataMapper.insert(dictData);
    }

    /**
     * 修改字典数据
     *
     * @param dto 字典数据DTO
     * @throws RuntimeException 当字典数据ID为空、不存在或字典值冲突时抛出异常
     */
    @Override
    public void updateDictData(SysDictDataDTO dto) {
        if (dto.getId() == null) {
            throw new RuntimeException("字典数据ID不能为空");
        }
        SysDictData dictData = sysDictDataMapper.selectById(dto.getId());
        if (dictData == null) {
            throw new RuntimeException("字典数据不存在");
        }
        // 检查字典值是否与其他记录冲突
        SysDictData existing = sysDictDataMapper.selectByTypeAndValue(dto.getDictType(), dto.getDictValue());
        if (existing != null && !existing.getId().equals(dto.getId())) {
            throw new RuntimeException("该字典类型下字典值已存在");
        }
        BeanUtils.copyProperties(dto, dictData);
        sysDictDataMapper.updateById(dictData);
    }

    /**
     * 删除字典数据
     *
     * @param id 字典数据ID
     * @throws RuntimeException 当字典数据不存在时抛出异常
     */
    @Override
    public void deleteDictData(Long id) {
        SysDictData dictData = sysDictDataMapper.selectById(id);
        if (dictData == null) {
            throw new RuntimeException("字典数据不存在");
        }
        sysDictDataMapper.deleteById(id);
    }
    /**
     * 分页查询字典类型
     */
    @Override
    public IPage<SysDictTypeVO> getDictTypePage(Integer pageNum, Integer pageSize, String dictName, String dictType) {
        Page<SysDictTypeVO> page = new Page<>(pageNum, pageSize);
        return sysDictTypeMapper.selectDictTypePage(page, dictName, dictType);
    }
}
