package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.SysDictData;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title 数据字典数据Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典数据数据访问接口
 */
@Mapper
public interface SysDictDataMapper extends BaseMapper<SysDictData> {

    // 根据字典类型查询数据列表
    List<SysDictData> selectByDictType(@Param("dictType") String dictType);

    // 根据字典类型和字典值查询
    SysDictData selectByTypeAndValue(@Param("dictType") String dictType, @Param("dictValue") String dictValue);

    // 删除指定字典类型的所有数据
    int deleteByDictType(@Param("dictType") String dictType);
}
