package com.hospital.registration.mapper;

import com.hospital.registration.entity.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @title: DepartmentRepository
 * @author: Su
 * @date: 2026/1/11 20:00
 * @version: 1.0
 * @description: 科室数据访问接口
 */
@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {
    // 根据科室编码查询
    Department findByCode(String code);
}
