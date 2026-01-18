package com.hospital.registration.service;

import com.hospital.registration.entity.Department;

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
     * 查询所有科室列表
     * @return 科室列表
     */
    List<Department> getAllDepartments();

    /**
     * 根据ID查询科室
     * @param id 科室ID
     * @return 科室信息
     */
    Department getDepartmentById(Long id);
}

