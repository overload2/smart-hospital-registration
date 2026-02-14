package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.DoctorDTO;
import com.hospital.registration.entity.Department;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.entity.User;
import com.hospital.registration.mapper.DepartmentMapper;
import com.hospital.registration.mapper.DoctorMapper;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.service.DoctorService;
import com.hospital.registration.vo.DoctorVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: DoctorServiceImpl
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 医生服务实现类
 */
@Slf4j
@Service
public class DoctorServiceImpl implements DoctorService {

    private final DoctorMapper doctorMapper;
    private final UserMapper userMapper;
    private final DepartmentMapper departmentMapper;

    /**
     * 构造器注入
     */
    public DoctorServiceImpl(DoctorMapper doctorMapper,
                             UserMapper userMapper,
                             DepartmentMapper departmentMapper) {
        this.doctorMapper = doctorMapper;
        this.userMapper = userMapper;
        this.departmentMapper = departmentMapper;
    }

    /**
     * 新增医生
     *
     * @param doctorDTO 医生信息DTO
     * @return 新增后的医生VO
     * @throws BusinessException 当用户不存在、科室不存在或用户已绑定医生时抛出异常
     */
    @Override
    public DoctorVO addDoctor(DoctorDTO doctorDTO) {
        log.info("新增医生 - 用户ID: {}, 科室ID: {}", doctorDTO.getUserId(), doctorDTO.getDepartmentId());

        // 检查用户是否存在
        User user = userMapper.selectById(doctorDTO.getUserId());
        if (user == null) {
            log.warn("用户不存在 - 用户ID: {}", doctorDTO.getUserId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        // 检查科室是否存在
        Department department = departmentMapper.selectById(doctorDTO.getDepartmentId());
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", doctorDTO.getDepartmentId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 检查该用户是否已经绑定医生信息
        Doctor existingDoctor = doctorMapper.selectByUserId(doctorDTO.getUserId());
        if (existingDoctor != null) {
            log.warn("该用户已绑定医生信息 - 用户ID: {}, 医生ID: {}", doctorDTO.getUserId(), existingDoctor.getId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "该用户已绑定医生信息");
        }

        // 创建医生实体
        Doctor doctor = new Doctor();
        BeanUtils.copyProperties(doctorDTO, doctor);
        doctor.setStatus(1); // 默认启用状态

        // 保存到数据库
        int result = doctorMapper.insert(doctor);
        if (result <= 0) {
            log.error("医生新增失败 - 用户ID: {}", doctorDTO.getUserId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "医生新增失败");
        }

        log.info("医生新增成功 - ID: {}, 用户姓名: {}", doctor.getId(), user.getRealName());

        // 查询并返回完整的医生信息(包含关联的用户和科室信息)
        return doctorMapper.selectDetailById(doctor.getId());
    }

    /**
     * 更新医生信息
     *
     * @param id 医生ID
     * @param doctorDTO 医生信息DTO
     * @return 更新后的医生VO
     * @throws BusinessException 当医生不存在、用户不存在、科室不存在或用户已绑定其他医生时抛出异常
     */
    @Override
    public DoctorVO updateDoctor(Long id, DoctorDTO doctorDTO) {
        log.info("更新医生信息 - ID: {}, 用户ID: {}", id, doctorDTO.getUserId());

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(id);
        if (doctor == null) {
            log.warn("医生不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        // 检查用户是否存在
        User user = userMapper.selectById(doctorDTO.getUserId());
        if (user == null) {
            log.warn("用户不存在 - 用户ID: {}", doctorDTO.getUserId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        // 检查科室是否存在
        Department department = departmentMapper.selectById(doctorDTO.getDepartmentId());
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", doctorDTO.getDepartmentId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 检查该用户是否已经绑定其他医生信息
        Doctor duplicateDoctor = doctorMapper.selectByUserIdExcludeId(doctorDTO.getUserId(), id);
        if (duplicateDoctor != null) {
            log.warn("该用户已绑定其他医生信息 - 用户ID: {}, 冲突医生ID: {}", doctorDTO.getUserId(), duplicateDoctor.getId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "该用户已绑定其他医生信息");
        }

        // 更新医生信息
        BeanUtils.copyProperties(doctorDTO, doctor);
        int result = doctorMapper.updateById(doctor);
        if (result <= 0) {
            log.error("医生更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "医生更新失败");
        }

        log.info("医生更新成功 - ID: {}, 用户姓名: {}", id, user.getRealName());

        // 查询并返回完整的医生信息
        return doctorMapper.selectDetailById(id);
    }

    /**
     * 删除医生
     *
     * @param id 医生ID
     * @throws BusinessException 当医生不存在时抛出异常
     */
    @Override
    public void deleteDoctor(Long id) {
        log.info("删除医生 - ID: {}", id);

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(id);
        if (doctor == null) {
            log.warn("医生不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        // TODO: 检查是否有未完成的排班或挂号,如果有则不能删除
        // 这里可以添加业务逻辑检查

        // 执行逻辑删除
        int result = doctorMapper.deleteById(id);
        if (result <= 0) {
            log.error("医生删除失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "医生删除失败");
        }

        log.info("医生删除成功 - ID: {}", id);
    }

    /**
     * 根据ID查询医生详情
     *
     * @param id 医生ID
     * @return 医生VO(包含用户和科室信息)
     * @throws BusinessException 当医生不存在时抛出异常
     */
    @Override
    public DoctorVO getDoctorById(Long id) {
        log.info("查询医生详情 - ID: {}", id);

        DoctorVO doctorVO = doctorMapper.selectDetailById(id);
        if (doctorVO == null) {
            log.warn("医生不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        log.info("查询医生成功 - ID: {}, 姓名: {}", id, doctorVO.getRealName());
        return doctorVO;
    }

    /**
     * 分页查询医生列表
     *
     * @param pageNum 页码(从1开始)
     * @param pageSize 每页大小
     * @param keyword 搜索关键词(可选,支持姓名、职称、专长模糊查询)
     * @param departmentId 科室ID(可选)
     * @param status 状态(可选)
     * @return 医生VO分页对象
     */
    @Override
    public Page<DoctorVO> getDoctorPage(Integer pageNum, Integer pageSize,
                                        String keyword, Long departmentId, Integer status) {
        log.info("分页查询医生列表 - 页码: {}, 每页大小: {}, 关键词: {}, 科室ID: {}, 状态: {}",
                pageNum, pageSize, keyword, departmentId, status);

        // 创建分页对象
        Page<DoctorVO> page = new Page<>(pageNum, pageSize);

        // 执行分页查询(通过XML自定义SQL,关联用户和科室表)
        Page<DoctorVO> doctorPage = doctorMapper.selectPageWithDetails(page, keyword, departmentId, status);

        log.info("查询到 {} 条医生记录,共 {} 页", doctorPage.getRecords().size(), doctorPage.getPages());
        return doctorPage;
    }

    /**
     * 查询指定科室的所有启用医生
     *
     * @param departmentId 科室ID
     * @return 医生VO列表(按评分和接诊数降序排列)
     */
    @Override
    public List<DoctorVO> getActiveDoctorsByDepartment(Long departmentId) {
        log.info("查询科室的启用医生列表 - 科室ID: {}", departmentId);

        // 检查科室是否存在
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", departmentId);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        List<DoctorVO> doctors = doctorMapper.selectActiveDoctorsByDepartment(departmentId);
        log.info("查询到 {} 个启用的医生", doctors.size());
        return doctors;
    }

    /**
     * 更新医生状态
     *
     * @param id 医生ID
     * @param status 状态(0-禁用,1-启用)
     * @throws BusinessException 当医生不存在时抛出异常
     */
    @Override
    public void updateDoctorStatus(Long id, Integer status) {
        log.info("更新医生状态 - ID: {}, 状态: {}", id, status);

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(id);
        if (doctor == null) {
            log.warn("医生不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        // 更新状态
        // 创建新的医生对象,只设置需要更新的字段
        Doctor updateDoctor = new Doctor();
        updateDoctor.setId(id);
        updateDoctor.setStatus(status);

        int result = doctorMapper.updateById(updateDoctor);
        if (result <= 0) {
            log.error("医生状态更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "医生状态更新失败");
        }

        log.info("医生状态更新成功 - ID: {}, 新状态: {}", id, status);
    }
}
