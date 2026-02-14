package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.DepartmentDTO;
import com.hospital.registration.vo.DepartmentVO;

import java.util.List;

/**
 * @title: DepartmentService
 * @author: Su
 * @date: 2026/1/12 18:30
 * @version: 1.0
 * @description: 科室服务接口
 */
public interface DepartmentService {

    /**
     * 获取所有启用的科室列表
     */
    List<DepartmentVO> getActiveDepartments();


    /**
     * 根据ID查询科室
     * @param id 科室ID
     * @return 科室信息
     */
    DepartmentVO getDepartmentById(Long id);

    /**
     * 新增科室
     */
    DepartmentVO addDepartment(DepartmentDTO departmentDTO);

    /**
     * 更新科室信息
     */
    DepartmentVO updateDepartment(Long id, DepartmentDTO departmentDTO);

    /**
     * 删除科室
     */
    void deleteDepartment(Long id);


    /**
     * 分页查询科室列表
     */
    Page<DepartmentVO> getDepartmentPage(Integer pageNum, Integer pageSize, String keyword);


    /**
     * 更新科室状态
     */
    void updateDepartmentStatus(Long id, Integer status);
}

