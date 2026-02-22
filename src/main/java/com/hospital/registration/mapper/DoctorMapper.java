package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Doctor;
import com.hospital.registration.vo.DoctorVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.time.LocalDate;
import java.util.List;

/**
* @author suzd
* @description 针对表【doctor(医生表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.Doctor
*/
@Mapper
public interface DoctorMapper extends BaseMapper<Doctor> {

    int deleteByPrimaryKey(Long id);

    int insert(Doctor record);

    int insertSelective(Doctor record);

    Doctor selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Doctor record);

    int updateByPrimaryKey(Doctor record);

    /**
     * 根据用户ID查询医生信息
     * @param userId 用户ID
     * @return 医生信息
     */
    Doctor selectByUserId(@Param("userId") Long userId);

    /**
     * 根据用户ID查询医生信息(排除指定医生ID)
     * @param userId 用户ID
     * @param excludeId 排除的医生ID
     * @return 医生信息
     */
    Doctor selectByUserIdExcludeId(@Param("userId") Long userId, @Param("excludeId") Long excludeId);

    /**
     * 分页查询医生列表(关联用户和科室信息)
     * @param page 分页对象
     * @param keyword 搜索关键词(支持姓名、职称、专长)
     * @param departmentId 科室ID(可选)
     * @param status 状态(可选)
     * @return 医生VO列表
     */
    Page<DoctorVO> selectPageWithDetails(Page<DoctorVO> page,
                                         @Param("keyword") String keyword,
                                         @Param("departmentId") Long departmentId,
                                         @Param("status") Integer status);

    /**
     * 根据ID查询医生详情(关联用户和科室信息)
     * @param id 医生ID
     * @return 医生VO
     */
    DoctorVO selectDetailById(@Param("id") Long id);

    /**
     * 查询指定科室的所有启用医生
     * @param departmentId 科室ID
     * @return 医生VO列表
     */
    List<DoctorVO> selectActiveDoctorsByDepartment(@Param("departmentId") Long departmentId);

    /**
     * 统计科室下的医生数量
     * @param departmentId 科室ID
     * @return 医生数量
     */
    Long countByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 批量更新医生状态
     * @param ids 医生ID列表
     * @param status 状态
     * @return 影响行数
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 查询科室下未来指定日期范围内有排班的医生
     */
    List<DoctorVO> selectDoctorsWithSchedule(@Param("departmentId") Long departmentId,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    /**
     * 统计医生数量
     */
    Integer countDoctors();
}
