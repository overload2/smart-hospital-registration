package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.SysOperationLog;
import com.hospital.registration.vo.SysOperationLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @title 操作日志Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 操作日志数据访问接口
 */
@Mapper
public interface SysOperationLogMapper extends BaseMapper<SysOperationLog> {

    // 分页条件查询操作日志
    Page<SysOperationLogVO> selectPageByCondition(Page<SysOperationLogVO> page,
                                                  @Param("username") String username,
                                                  @Param("module") String module,
                                                  @Param("operation") String operation,
                                                  @Param("status") Integer status,
                                                  @Param("startTime") String startTime,
                                                  @Param("endTime") String endTime);

    // 删除指定天数之前的日志
    int deleteByDaysBefore(@Param("days") int days);
}
