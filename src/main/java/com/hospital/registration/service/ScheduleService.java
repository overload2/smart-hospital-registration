package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.ScheduleDTO;
import com.hospital.registration.vo.ScheduleVO;
import com.hospital.registration.vo.app.DetailTimeSlotVO;

import java.time.LocalDate;
import java.util.List;

/**
 * @title: ScheduleService
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 排班服务接口
 */
public interface ScheduleService {

    /**
     * 新增排班
     */
    ScheduleVO addSchedule(ScheduleDTO scheduleDTO);

    /**
     * 更新排班信息
     */
    ScheduleVO updateSchedule(Long id, ScheduleDTO scheduleDTO);

    /**
     * 删除排班
     */
    void deleteSchedule(Long id);

    /**
     * 根据ID查询排班详情
     */
    ScheduleVO getScheduleById(Long id);

    /**
     * 分页查询排班列表
     */
    Page<ScheduleVO> getSchedulePage(Integer pageNum, Integer pageSize,
                                     Long doctorId, Long departmentId,
                                     LocalDate scheduleDate, Integer status);

    /**
     * 查询指定医生的可预约排班列表
     */
    List<ScheduleVO> getAvailableSchedulesByDoctor(Long doctorId, LocalDate startDate, LocalDate endDate);

    /**
     * 查询指定科室的可预约排班列表
     */
    List<ScheduleVO> getAvailableSchedulesByDepartment(Long departmentId, LocalDate startDate, LocalDate endDate);

    /**
     * 更新排班状态
     */
    void updateScheduleStatus(Long id, Integer status);

    /**
     * 批量创建排班
     */
    List<ScheduleVO> batchAddSchedules(List<ScheduleDTO> scheduleDTOList);

    List<DetailTimeSlotVO> getDetailSlots(Long id, Long userId);
}
