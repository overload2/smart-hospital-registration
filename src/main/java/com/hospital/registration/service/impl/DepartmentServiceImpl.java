package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.DepartmentDTO;
import com.hospital.registration.entity.Department;
import com.hospital.registration.mapper.DepartmentMapper;
import com.hospital.registration.service.DepartmentService;
import com.hospital.registration.vo.DepartmentVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
     * 新增科室
     *
     * @param departmentDTO 科室信息DTO
     * @return 新增后的科室VO
     * @throws BusinessException 当科室编码已存在时抛出异常
     */
    @Override
    public DepartmentVO addDepartment(DepartmentDTO departmentDTO) {
        log.info("新增科室 - 科室名称: {}, 科室编码: {}", departmentDTO.getName(), departmentDTO.getCode());

        // 检查科室编码是否已存在
        Department existingDept = departmentMapper.selectByCode(departmentDTO.getCode());
        if (existingDept != null) {
            log.warn("科室编码已存在 - 编码: {}", departmentDTO.getCode());
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室编码已存在");
        }

        // 创建科室实体
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        department.setStatus(1); // 默认启用状态

        // 保存到数据库
        int result = departmentMapper.insert(department);
        if (result <= 0) {
            log.error("科室新增失败 - 科室编码: {}", departmentDTO.getCode());
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室新增失败");
        }

        log.info("科室新增成功 - ID: {}, 科室名称: {}", department.getId(), department.getName());
        return convertToVO(department);
    }

    /**
     * 更新科室信息
     *
     * @param id 科室ID
     * @param departmentDTO 科室信息DTO
     * @return 更新后的科室VO
     * @throws BusinessException 当科室不存在或编码重复时抛出异常
     */
    @Override
    public DepartmentVO updateDepartment(Long id, DepartmentDTO departmentDTO) {
        log.info("更新科室信息 - ID: {}, 科室名称: {}", id, departmentDTO.getName());

        // 检查科室是否存在
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            log.warn("科室不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 检查科室编码是否与其他科室重复
        Department duplicateDept = departmentMapper.selectByCodeExcludeId(departmentDTO.getCode(), id);
        if (duplicateDept != null) {
            log.warn("科室编码已被其他科室使用 - 编码: {}, 冲突科室ID: {}", departmentDTO.getCode(), duplicateDept.getId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室编码已被其他科室使用");
        }

        // 更新科室信息
        BeanUtils.copyProperties(departmentDTO, department);
        int result = departmentMapper.updateById(department);
        if (result <= 0) {
            log.error("科室更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室更新失败");
        }

        log.info("科室更新成功 - ID: {}, 科室名称: {}", id, department.getName());
        return convertToVO(department);
    }

    /**
     * 删除科室
     *
     * @param id 科室ID
     * @throws BusinessException 当科室不存在时抛出异常
     */
    @Override
    public void deleteDepartment(Long id) {
        log.info("删除科室 - ID: {}", id);

        // 检查科室是否存在
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            log.warn("科室不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // TODO: 检查是否有医生关联此科室,如果有则不能删除
        // 这里可以添加业务逻辑检查,例如:
        // Long doctorCount = doctorMapper.countByDepartmentId(id);
        // if (doctorCount > 0) {
        //     throw new BusinessException(ResultCode.FAIL.getCode(), "该科室下有医生,无法删除");
        // }

        // 执行删除
        int result = departmentMapper.deleteById(id);
        if (result <= 0) {
            log.error("科室删除失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室删除失败");
        }

        log.info("科室删除成功 - ID: {}", id);
    }

    /**
     * 根据ID查询科室详情
     *
     * @param id 科室ID
     * @return 科室VO
     * @throws BusinessException 当科室不存在时抛出异常
     */
    @Override
    public DepartmentVO getDepartmentById(Long id) {
        log.info("查询科室详情 - ID: {}", id);

        Department department = departmentMapper.selectById(id);
        if (department == null) {
            log.warn("科室不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        log.info("查询科室成功 - ID: {}, 科室名称: {}", id, department.getName());
        return convertToVO(department);
    }

    /**
     * 分页查询科室列表(支持关键词搜索)
     *
     * @param pageNum 页码(从1开始)
     * @param pageSize 每页大小
     * @param keyword 搜索关键词(可选,支持科室名称和编码模糊查询)
     * @return 科室VO分页对象
     */
    @Override
    public Page<DepartmentVO> getDepartmentPage(Integer pageNum, Integer pageSize, String keyword) {
        log.info("分页查询科室列表 - 页码: {}, 每页大小: {}, 关键词: {}", pageNum, pageSize, keyword);

        // 创建分页对象
        Page<Department> page = new Page<>(pageNum, pageSize);

        // 执行分页查询(通过XML自定义SQL)
        Page<Department> departmentPage = departmentMapper.selectPageByKeyword(page, keyword);

        // 转换为VO分页对象
        Page<DepartmentVO> voPage = new Page<>(
                departmentPage.getCurrent(),
                departmentPage.getSize(),
                departmentPage.getTotal()
        );

        List<DepartmentVO> voList = departmentPage.getRecords().stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());
        voPage.setRecords(voList);

        log.info("查询到 {} 条科室记录,共 {} 页", departmentPage.getRecords().size(), departmentPage.getPages());
        return voPage;
    }

    /**
     * 获取所有启用的科室列表
     *
     * @return 启用的科室VO列表(按名称升序排列)
     */
    @Override
    public List<DepartmentVO> getActiveDepartments() {
        log.info("查询所有启用的科室列表");

        // 查询所有启用的科室(通过XML自定义SQL)
        List<Department> departments = departmentMapper.selectActiveList();

        List<DepartmentVO> voList = departments.stream()
                .map(this::convertToVO)
                .collect(Collectors.toList());

        log.info("查询到 {} 个启用的科室", voList.size());
        return voList;
    }

    /**
     * 更新科室状态
     *
     * @param id 科室ID
     * @param status 状态(0-禁用,1-启用)
     * @throws BusinessException 当科室不存在时抛出异常
     */
    @Override
    public void updateDepartmentStatus(Long id, Integer status) {
        log.info("更新科室状态 - ID: {}, 状态: {}", id, status);

        // 检查科室是否存在
        Department department = departmentMapper.selectById(id);
        if (department == null) {
            log.warn("科室不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 更新状态
        Department updateDepartment = new Department() ;
        updateDepartment.setId(id);
        updateDepartment.setStatus(status);
        int result = departmentMapper.updateById(updateDepartment);

        if (result <= 0) {
            log.error("科室状态更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "科室状态更新失败");
        }

        log.info("科室状态更新成功 - ID: {}, 新状态: {}", id, status);
    }

    /**
     * 将Department实体转换为DepartmentVO
     */
    private DepartmentVO convertToVO(Department department) {
        DepartmentVO vo = new DepartmentVO();
        BeanUtils.copyProperties(department, vo);
        return vo;
    }
}

