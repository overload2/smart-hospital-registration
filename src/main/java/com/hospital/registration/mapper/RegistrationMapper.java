package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.vo.RegistrationVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * @author suzd
 * @description 针对表【registration(挂号记录表)】的数据库操作Mapper
 * @createDate 2026-01-18 18:55:48
 * @Entity com.hospital.registration.entity.Registration
 */
@Mapper
public interface RegistrationMapper extends BaseMapper<Registration> {

    /**
     * 根据挂号单号查询挂号记录
     * @param registrationNo 挂号单号
     * @return 挂号记录
     */
    Registration selectByRegistrationNo(@Param("registrationNo") String registrationNo);

    /**
     * 查询指定排班的当前最大排队号
     * @param scheduleId 排班ID
     * @return 最大排队号
     */
    Integer selectMaxQueueNumber(@Param("scheduleId") Long scheduleId);

    /**
     * 分页查询挂号列表(关联患者、医生、科室信息)
     * @param page 分页对象
     * @param patientId 患者ID(可选)
     * @param doctorId 医生ID(可选)
     * @param departmentId 科室ID(可选)
     * @param registrationDate 挂号日期(可选)
     * @param status 挂号状态(可选)
     * @return 挂号VO列表
     */
    Page<RegistrationVO> selectPageWithDetails(Page<RegistrationVO> page,
                                               @Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("departmentId") Long departmentId,
                                               @Param("registrationDate") LocalDate registrationDate,
                                               @Param("status") String status);

    /**
     * 根据ID查询挂号详情(关联患者、医生、科室信息)
     * @param id 挂号ID
     * @return 挂号VO
     */
    RegistrationVO selectDetailById(@Param("id") Long id);

    /**
     * 查询患者的挂号记录列表
     * @param patientId 患者ID
     * @return 挂号VO列表
     */
    List<RegistrationVO> selectByPatientId(@Param("patientId") Long patientId);

    /**
     * 查询医生的挂号记录列表
     * @param doctorId 医生ID
     * @param registrationDate 挂号日期
     * @return 挂号VO列表
     */
    List<RegistrationVO> selectByDoctorAndDate(@Param("doctorId") Long doctorId,
                                               @Param("registrationDate") LocalDate registrationDate);

    /**
     * 统计指定排班的挂号数量
     * @param scheduleId 排班ID
     * @return 挂号数量
     */
    Long countByScheduleId(@Param("scheduleId") Long scheduleId);

    /**
     * 查询医生今日待诊列表
     */
    List<RegistrationVO> selectDoctorTodayPatients(@Param("doctorId") Long doctorId,
                                                   @Param("registrationDate") LocalDate registrationDate);

    /**
     * 统计排班下各细分时段的已预约数量
     */
    List<Map<String, Object>> countByScheduleAndDetailSlot(@Param("scheduleId") Long scheduleId);

    /**
     * 查询用户在指定排班下已预约的细分时段
     */
    List<String> selectBookedDetailSlots(@Param("patientId") Long patientId,
                                         @Param("scheduleId") Long scheduleId);


    /**
     * 分页查询挂号列表（增强版）
     */
    Page<RegistrationVO> selectPageWithDetails(Page<RegistrationVO> page,
                                               @Param("patientId") Long patientId,
                                               @Param("doctorId") Long doctorId,
                                               @Param("departmentId") Long departmentId,
                                               @Param("registrationDate") LocalDate registrationDate,
                                               @Param("status") String status,
                                               @Param("registrationNo") String registrationNo,
                                               @Param("patientName") String patientName,
                                               @Param("patientPhone") String patientPhone,
                                               @Param("patientIdCard") String patientIdCard);

    /**
     * 查询今日候诊队列
     */
    List<RegistrationVO> selectTodayQueue(@Param("doctorId") Long doctorId,
                                          @Param("registrationDate") LocalDate registrationDate);

    /**
     * 查询当前叫号信息
     */
    RegistrationVO selectCurrentCalled(@Param("doctorId") Long doctorId,
                                       @Param("registrationDate") LocalDate registrationDate);

    /**
     * 统计今日挂号数
     */
    Integer countTodayRegistrations();

    /**
     * 统计待就诊数（PENDING + CALLED）
     */
    Integer countPendingRegistrations();

    /**
     * 获取近7天挂号趋势
     */
    List<Map<String, Object>> selectRegistrationTrend();

    /**
     * 获取科室挂号占比
     */
    List<Map<String, Object>> selectDepartmentRatio();

    /**
     * 查询所有今日待诊患者（管理员用）
     * @param date 日期
     * @return 挂号VO列表
     */
    List<RegistrationVO> selectAllTodayPatients(@Param("date") LocalDate date);
}

