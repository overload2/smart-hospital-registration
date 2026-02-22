package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.SysLoginLog;
import com.hospital.registration.vo.SysLoginLogVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * @title 登录日志Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 登录日志数据访问接口
 */
@Mapper
public interface SysLoginLogMapper extends BaseMapper<SysLoginLog> {

    // 分页条件查询登录日志
    Page<SysLoginLogVO> selectPageByCondition(Page<SysLoginLogVO> page,
                                              @Param("username") String username,
                                              @Param("status") Integer status,
                                              @Param("ip") String ip,
                                              @Param("startTime") String startTime,
                                              @Param("endTime") String endTime);

    // 删除指定天数之前的日志
    int deleteByDaysBefore(@Param("days") int days);
}
