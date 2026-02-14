package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Department;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author suzd
* @description 针对表【department(科室表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.Department
*/
@Mapper
public interface DepartmentMapper extends BaseMapper<Department> {

    int deleteByPrimaryKey(Long id);

    int insert(Department record);

    int insertSelective(Department record);

    Department selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Department record);

    int updateByPrimaryKey(Department record);

    /**
     * 根据科室编码查询科室
     * @param code 科室编码
     * @return 科室信息
     */
    Department selectByCode(@Param("code") String code);

    /**
     * 根据科室编码查询科室(排除指定ID)
     * @param code 科室编码
     * @param excludeId 排除的科室ID
     * @return 科室信息
     */
    Department selectByCodeExcludeId(@Param("code") String code, @Param("excludeId") Long excludeId);

    /**
     * 分页查询科室列表(支持关键词搜索)
     * @param page 分页对象
     * @param keyword 搜索关键词
     * @return 科室列表
     */
    Page<Department> selectPageByKeyword(Page<Department> page, @Param("keyword") String keyword);

    /**
     * 查询所有启用的科室
     * @return 科室列表
     */
    List<Department> selectActiveList();
}
