package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.SysDictType;
import com.hospital.registration.vo.SysDictTypeVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title 数据字典类型Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典类型数据访问接口
 */
@Mapper
public interface SysDictTypeMapper extends BaseMapper<SysDictType> {

    // 根据字典类型编码查询
    SysDictType selectByDictType(@Param("dictType") String dictType);

    // 查询所有启用的字典类型
    List<SysDictType> selectAllEnabled();
    /**
     * 分页查询字典类型
     */
    IPage<SysDictTypeVO> selectDictTypePage(Page<SysDictTypeVO> page,
                                            @Param("dictName") String dictName,
                                            @Param("dictType") String dictType);
}
