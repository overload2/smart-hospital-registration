package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.DetailTimeSlot;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.ScheduleDTO;
import com.hospital.registration.entity.Department;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.entity.Schedule;
import com.hospital.registration.mapper.DepartmentMapper;
import com.hospital.registration.mapper.DoctorMapper;
import com.hospital.registration.mapper.RegistrationMapper;
import com.hospital.registration.mapper.ScheduleMapper;
import com.hospital.registration.service.ScheduleService;
import com.hospital.registration.vo.ScheduleVO;
import com.hospital.registration.vo.app.DetailTimeSlotVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @title: ScheduleServiceImpl
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 排班服务实现类
 */
@Slf4j
@Service
public class ScheduleServiceImpl implements ScheduleService {

    private final ScheduleMapper scheduleMapper;
    private final DoctorMapper doctorMapper;
    private final DepartmentMapper departmentMapper;
    private final RegistrationMapper registrationMapper;

    /**
     * 构造器注入
     */
    public ScheduleServiceImpl(ScheduleMapper scheduleMapper,
                               DoctorMapper doctorMapper,
                               DepartmentMapper departmentMapper,
                               RegistrationMapper registrationMapper) {
        this.scheduleMapper = scheduleMapper;
        this.doctorMapper = doctorMapper;
        this.departmentMapper = departmentMapper;
        this.registrationMapper = registrationMapper;
    }

    /**
     * 新增排班
     *
     * @param scheduleDTO 排班信息DTO
     * @return 新增后的排班VO
     * @throws BusinessException 当医生不存在、科室不存在或排班冲突时抛出异常
     */
    @Override
    public ScheduleVO addSchedule(ScheduleDTO scheduleDTO) {
        log.info("新增排班 - 医生ID: {}, 科室ID: {}, 日期: {}, 时间段: {}",
                scheduleDTO.getDoctorId(), scheduleDTO.getDepartmentId(),
                scheduleDTO.getScheduleDate(), scheduleDTO.getTimeSlot());

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(scheduleDTO.getDoctorId());
        if (doctor == null) {
            log.warn("医生不存在 - 医生ID: {}", scheduleDTO.getDoctorId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        // 检查科室是否存在
        Department department = departmentMapper.selectById(scheduleDTO.getDepartmentId());
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", scheduleDTO.getDepartmentId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 检查该医生在该日期和时间段是否已有排班
        Schedule existingSchedule = scheduleMapper.selectByDoctorDateAndTime(
                scheduleDTO.getDoctorId(),
                scheduleDTO.getScheduleDate(),
                scheduleDTO.getTimeSlot().getDbValue()
        );
        if (existingSchedule != null) {
            log.warn("该医生在该时间段已有排班 - 医生ID: {}, 日期: {}, 时间段: {}",
                    scheduleDTO.getDoctorId(), scheduleDTO.getScheduleDate(), scheduleDTO.getTimeSlot());
            throw new BusinessException(ResultCode.FAIL.getCode(), "该医生在该时间段已有排班");
        }

        // 创建排班实体
        Schedule schedule = new Schedule();
        BeanUtils.copyProperties(scheduleDTO, schedule);
        schedule.setRemainingNumber(scheduleDTO.getTotalNumber()); // 初始剩余号源等于总号源
        schedule.setStatus(1); // 默认可预约状态

        // 保存到数据库
        int result = scheduleMapper.insert(schedule);
        if (result <= 0) {
            log.error("排班新增失败 - 医生ID: {}", scheduleDTO.getDoctorId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "排班新增失败");
        }

        log.info("排班新增成功 - ID: {}, 医生ID: {}", schedule.getId(), schedule.getDoctorId());

        // 查询并返回完整的排班信息
        return scheduleMapper.selectDetailById(schedule.getId());
    }

    /**
     * 更新排班信息
     *
     * @param id 排班ID
     * @param scheduleDTO 排班信息DTO
     * @return 更新后的排班VO
     * @throws BusinessException 当排班不存在、医生不存在、科室不存在或排班冲突时抛出异常
     */
    @Override
    public ScheduleVO updateSchedule(Long id, ScheduleDTO scheduleDTO) {
        log.info("更新排班信息 - ID: {}, 医生ID: {}", id, scheduleDTO.getDoctorId());

        // 检查排班是否存在
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            log.warn("排班不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(scheduleDTO.getDoctorId());
        if (doctor == null) {
            log.warn("医生不存在 - 医生ID: {}", scheduleDTO.getDoctorId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        // 检查科室是否存在
        Department department = departmentMapper.selectById(scheduleDTO.getDepartmentId());
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", scheduleDTO.getDepartmentId());
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        // 检查该医生在该日期和时间段是否已有其他排班
        Schedule duplicateSchedule = scheduleMapper.selectByDoctorDateAndTimeExcludeId(
                scheduleDTO.getDoctorId(),
                scheduleDTO.getScheduleDate(),
                scheduleDTO.getTimeSlot().getDbValue(),
                id
        );
        if (duplicateSchedule != null) {
            log.warn("该医生在该时间段已有其他排班 - 医生ID: {}, 日期: {}, 时间段: {}, 冲突排班ID: {}",
                    scheduleDTO.getDoctorId(), scheduleDTO.getScheduleDate(),
                    scheduleDTO.getTimeSlot(), duplicateSchedule.getId());
            throw new BusinessException(ResultCode.FAIL.getCode(), "该医生在该时间段已有其他排班");
        }

        // 计算已预约数量
        int bookedNumber = schedule.getTotalNumber() - schedule.getRemainingNumber();

        // 检查新的总号源数是否小于已预约数量
        if (scheduleDTO.getTotalNumber() < bookedNumber) {
            log.warn("新的总号源数不能小于已预约数量 - 已预约: {}, 新总号源: {}",
                    bookedNumber, scheduleDTO.getTotalNumber());
            throw new BusinessException(ResultCode.FAIL.getCode(),
                    "新的总号源数不能小于已预约数量(" + bookedNumber + ")");
        }

        // 更新排班信息
        BeanUtils.copyProperties(scheduleDTO, schedule);
        // 重新计算剩余号源数
        schedule.setRemainingNumber(scheduleDTO.getTotalNumber() - bookedNumber);

        int result = scheduleMapper.updateById(schedule);
        if (result <= 0) {
            log.error("排班更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "排班更新失败");
        }

        log.info("排班更新成功 - ID: {}", id);

        // 查询并返回完整的排班信息
        return scheduleMapper.selectDetailById(id);
    }

    /**
     * 删除排班
     *
     * @param id 排班ID
     * @throws BusinessException 当排班不存在或已有预约时抛出异常
     */
    @Override
    public void deleteSchedule(Long id) {
        log.info("删除排班 - ID: {}", id);

        // 检查排班是否存在
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            log.warn("排班不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }

        // 检查是否有预约记录
        int bookedNumber = schedule.getTotalNumber() - schedule.getRemainingNumber();
        if (bookedNumber > 0) {
            log.warn("该排班已有预约记录,无法删除 - ID: {}, 已预约数: {}", id, bookedNumber);
            throw new BusinessException(ResultCode.FAIL.getCode(), "该排班已有预约记录,无法删除");
        }

        // 执行逻辑删除
        int result = scheduleMapper.deleteById(id);
        if (result <= 0) {
            log.error("排班删除失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "排班删除失败");
        }

        log.info("排班删除成功 - ID: {}", id);
    }

    /**
     * 根据ID查询排班详情
     *
     * @param id 排班ID
     * @return 排班VO(包含医生和科室信息)
     * @throws BusinessException 当排班不存在时抛出异常
     */
    @Override
    public ScheduleVO getScheduleById(Long id) {
        log.info("查询排班详情 - ID: {}", id);

        ScheduleVO scheduleVO = scheduleMapper.selectDetailById(id);
        if (scheduleVO == null) {
            log.warn("排班不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }

        // 设置时间段名称和时间范围
        enrichScheduleVO(scheduleVO);

        log.info("查询排班成功 - ID: {}, 医生: {}", id, scheduleVO.getDoctorName());
        return scheduleVO;
    }

    /**
     * 分页查询排班列表
     *
     * @param pageNum 页码(从1开始)
     * @param pageSize 每页大小
     * @param doctorId 医生ID(可选)
     * @param departmentId 科室ID(可选)
     * @param scheduleDate 排班日期(可选)
     * @param status 状态(可选)
     * @return 排班VO分页对象
     */
    @Override
    public Page<ScheduleVO> getSchedulePage(Integer pageNum, Integer pageSize,
                                            Long doctorId, Long departmentId,
                                            LocalDate scheduleDate, Integer status) {
        log.info("分页查询排班列表 - 页码: {}, 每页大小: {}, 医生ID: {}, 科室ID: {}, 日期: {}, 状态: {}",
                pageNum, pageSize, doctorId, departmentId, scheduleDate, status);

        // 创建分页对象
        Page<ScheduleVO> page = new Page<>(pageNum, pageSize);

        // 执行分页查询
        Page<ScheduleVO> schedulePage = scheduleMapper.selectPageWithDetails(
                page, doctorId, departmentId, scheduleDate, status);

        // 丰富VO信息
        schedulePage.getRecords().forEach(this::enrichScheduleVO);

        log.info("查询到 {} 条排班记录,共 {} 页", schedulePage.getRecords().size(), schedulePage.getPages());
        return schedulePage;
    }

    /**
     * 查询指定医生的可预约排班列表
     *
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班VO列表
     */
    @Override
    public List<ScheduleVO> getAvailableSchedulesByDoctor(Long doctorId, LocalDate startDate, LocalDate endDate) {
        log.info("查询医生的可预约排班列表 - 医生ID: {}, 开始日期: {}, 结束日期: {}",
                doctorId, startDate, endDate);

        // 检查医生是否存在
        Doctor doctor = doctorMapper.selectById(doctorId);
        if (doctor == null) {
            log.warn("医生不存在 - 医生ID: {}", doctorId);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "医生不存在");
        }

        List<ScheduleVO> schedules = scheduleMapper.selectAvailableByDoctor(doctorId, startDate, endDate);

        // 丰富VO信息
        schedules.forEach(this::enrichScheduleVO);

        log.info("查询到 {} 条可预约排班", schedules.size());
        return schedules;
    }

    /**
     * 查询指定科室的可预约排班列表
     *
     * @param departmentId 科室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班VO列表
     */
    @Override
    public List<ScheduleVO> getAvailableSchedulesByDepartment(Long departmentId, LocalDate startDate, LocalDate endDate) {
        log.info("查询科室的可预约排班列表 - 科室ID: {}, 开始日期: {}, 结束日期: {}",
                departmentId, startDate, endDate);

        // 检查科室是否存在
        Department department = departmentMapper.selectById(departmentId);
        if (department == null) {
            log.warn("科室不存在 - 科室ID: {}", departmentId);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "科室不存在");
        }

        List<ScheduleVO> schedules = scheduleMapper.selectAvailableByDepartment(departmentId, startDate, endDate);

        // 丰富VO信息
        schedules.forEach(this::enrichScheduleVO);

        log.info("查询到 {} 条可预约排班", schedules.size());
        return schedules;
    }

    /**
     * 更新排班状态
     *
     * @param id 排班ID
     * @param status 状态(0-已取消,1-可预约,2-已满)
     * @throws BusinessException 当排班不存在时抛出异常
     */
    @Override
    public void updateScheduleStatus(Long id, Integer status) {
        log.info("更新排班状态 - ID: {}, 状态: {}", id, status);

        // 检查排班是否存在
        Schedule schedule = scheduleMapper.selectById(id);
        if (schedule == null) {
            log.warn("排班不存在 - ID: {}", id);
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "排班不存在");
        }

        // 创建新的排班对象,只设置需要更新的字段
        Schedule updateSchedule = new Schedule();
        updateSchedule.setId(id);
        updateSchedule.setStatus(status);

        int result = scheduleMapper.updateById(updateSchedule);
        if (result <= 0) {
            log.error("排班状态更新失败 - ID: {}", id);
            throw new BusinessException(ResultCode.FAIL.getCode(), "排班状态更新失败");
        }

        log.info("排班状态更新成功 - ID: {}, 新状态: {}", id, status);
    }

    /**
     * 批量创建排班
     *
     * @param scheduleDTOList 排班信息DTO列表
     * @return 新增后的排班VO列表
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public List<ScheduleVO> batchAddSchedules(List<ScheduleDTO> scheduleDTOList) {
        log.info("批量创建排班 - 数量: {}", scheduleDTOList.size());

        List<ScheduleVO> resultList = new ArrayList<>();

        for (ScheduleDTO scheduleDTO : scheduleDTOList) {
            try {
                ScheduleVO scheduleVO = addSchedule(scheduleDTO);
                resultList.add(scheduleVO);
            } catch (BusinessException e) {
                log.warn("排班创建失败,跳过 - 医生ID: {}, 日期: {}, 原因: {}",
                        scheduleDTO.getDoctorId(), scheduleDTO.getScheduleDate(), e.getMessage());
                // 继续处理下一个排班
            }
        }

        log.info("批量创建排班完成 - 成功: {}, 失败: {}",
                resultList.size(), scheduleDTOList.size() - resultList.size());

        return resultList;
    }


    /**
     * 获取排班的细分时段列表（患者端）
     */
    @Override
    public List<DetailTimeSlotVO> getDetailSlots(Long scheduleId, Long patientId) {
        // 查询排班信息
        ScheduleVO schedule = scheduleMapper.selectDetailById(scheduleId);
        if (schedule == null) {
            return new ArrayList<>();
        }

        // 获取该大时段对应的细分时段配置
        List<DetailTimeSlot> slotConfigs = DetailTimeSlot.getSlotsByTimeSlot(schedule.getTimeSlot());

        // 每个细分时段的容量
        Integer slotCapacity = schedule.getSlotCapacity() != null ? schedule.getSlotCapacity() : 5;

        // 查询各细分时段已预约数量
        List<Map<String, Object>> bookedCounts = registrationMapper.countByScheduleAndDetailSlot(scheduleId);
        Map<String, Integer> bookedMap = new HashMap<>();
        for (Map<String, Object> item : bookedCounts) {
            String slot = (String) item.get("detailTimeSlot");
            Integer count = ((Number) item.get("count")).intValue();
            bookedMap.put(slot, count);
        }

        // 查询用户已预约的细分时段
        List<String> userBookedSlots = new ArrayList<>();
        if (patientId != null) {
            userBookedSlots = registrationMapper.selectBookedDetailSlots(patientId, scheduleId);
        }

        // 构建细分时段VO列表
        List<DetailTimeSlotVO> result = new ArrayList<>();
        for (DetailTimeSlot config : slotConfigs) {
            DetailTimeSlotVO vo = new DetailTimeSlotVO();
            vo.setSlotCode(config.getSlotCode());
            vo.setTimeRange(config.getTimeRange());
            vo.setPeriod(config.getPeriod());
            vo.setCapacity(slotCapacity);

            Integer booked = bookedMap.getOrDefault(config.getSlotCode(), 0);
            vo.setBookedCount(booked);
            vo.setRemainingCount(slotCapacity - booked);

            // 判断是否可预约
            if (userBookedSlots.contains(config.getSlotCode())) {
                vo.setAvailable(false);
                vo.setUnavailableReason("BOOKED");
            } else if (vo.getRemainingCount() <= 0) {
                vo.setAvailable(false);
                vo.setUnavailableReason("FULL");
            } else {
                vo.setAvailable(true);
                vo.setUnavailableReason(null);
            }

            result.add(vo);
        }

        return result;
    }

    /**
     * 丰富ScheduleVO信息(添加时间段名称和时间范围)
     */
    private void enrichScheduleVO(ScheduleVO scheduleVO) {
        if (scheduleVO.getTimeSlot() != null) {
            scheduleVO.setTimeSlotName(scheduleVO.getTimeSlot().getName());
            scheduleVO.setTimeRange(scheduleVO.getTimeSlot().getTimeRange());
        }
    }
}

