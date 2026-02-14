package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Schedule;
import com.hospital.registration.vo.ScheduleVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
 * @author suzd
 * @description 针对表【schedule(排班表)】的数据库操作Mapper
 * @createDate 2026-01-18 18:55:48
 * @Entity com.hospital.registration.entity.Schedule
 */
@Mapper
public interface ScheduleMapper extends BaseMapper<Schedule> {


    /**
     * 查询指定医生在指定日期和时间段的排班
     * @param doctorId 医生ID
     * @param scheduleDate 排班日期
     * @param timeSlot 时间段
     * @return 排班信息
     */
    Schedule selectByDoctorDateAndTime(@Param("doctorId") Long doctorId,
                                       @Param("scheduleDate") LocalDate scheduleDate,
                                       @Param("timeSlot") String timeSlot);

    /**
     * 查询指定医生在指定日期和时间段的排班(排除指定ID)
     * @param doctorId 医生ID
     * @param scheduleDate 排班日期
     * @param timeSlot 时间段
     * @param excludeId 排除的排班ID
     * @return 排班信息
     */
    Schedule selectByDoctorDateAndTimeExcludeId(@Param("doctorId") Long doctorId,
                                                @Param("scheduleDate") LocalDate scheduleDate,
                                                @Param("timeSlot") String timeSlot,
                                                @Param("excludeId") Long excludeId);

    /**
     * 分页查询排班列表(关联医生和科室信息)
     * @param page 分页对象
     * @param doctorId 医生ID(可选)
     * @param departmentId 科室ID(可选)
     * @param scheduleDate 排班日期(可选)
     * @param status 状态(可选)
     * @return 排班VO列表
     */
    Page<ScheduleVO> selectPageWithDetails(Page<ScheduleVO> page,
                                           @Param("doctorId") Long doctorId,
                                           @Param("departmentId") Long departmentId,
                                           @Param("scheduleDate") LocalDate scheduleDate,
                                           @Param("status") Integer status);

    /**
     * 根据ID查询排班详情(关联医生和科室信息)
     * @param id 排班ID
     * @return 排班VO
     */
    ScheduleVO selectDetailById(@Param("id") Long id);

    /**
     * 查询指定医生的可预约排班列表
     * @param doctorId 医生ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班VO列表
     */
    List<ScheduleVO> selectAvailableByDoctor(@Param("doctorId") Long doctorId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * 查询指定科室的可预约排班列表
     * @param departmentId 科室ID
     * @param startDate 开始日期
     * @param endDate 结束日期
     * @return 排班VO列表
     */
    List<ScheduleVO> selectAvailableByDepartment(@Param("departmentId") Long departmentId,
                                                 @Param("startDate") LocalDate startDate,
                                                 @Param("endDate") LocalDate endDate);

    /**
     * 减少剩余号源数
     * @param id 排班ID
     * @return 影响行数
     */
    int decreaseRemainingNumber(@Param("id") Long id);

    /**
     * 增加剩余号源数
     * @param id 排班ID
     * @return 影响行数
     */
    int increaseRemainingNumber(@Param("id") Long id);
}

