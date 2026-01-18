package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.entity.Department;
import com.hospital.registration.mapper.DepartmentMapper;
import com.hospital.registration.service.DepartmentService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: DepartmentServiceImpl
 * @author: Su
 * @date: 2026/1/12 18:35
 * @version: 1.0
 * @description: 科室服务实现类
 */
@Slf4j
@Service
public class DepartmentServiceImpl implements DepartmentService {

    private final DepartmentMapper departmentMapper;

    // 构造器注入
    public DepartmentServiceImpl(DepartmentMapper departmentMapper) {
        this.departmentMapper = departmentMapper;
    }

    /**
     * 查询所有科室列表
     */
    @Override
    public List<Department> getAllDepartments() {
        log.info("查询所有科室列表");

        // 查询所有启用的科室
        List<Department> departments = departmentMapper.selectList(
                new QueryWrapper<Department>().eq("status", 1));

        log.info("查询到 {} 个科室", departments.size());
        return departments;
    }

    /**
     * 根据ID查询科室
     */
    @Override
    public Department getDepartmentById(Long id) {
        log.info("查询科室详情 - ID: {}", id);

        Department department = departmentMapper.selectById(id);
        if (department == null){
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }
        return department;
    }
}

