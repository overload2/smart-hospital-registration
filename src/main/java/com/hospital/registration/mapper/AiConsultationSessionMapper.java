package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.AiConsultationSession;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title AI问诊会话Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊会话数据访问接口
 */
@Mapper
public interface AiConsultationSessionMapper extends BaseMapper<AiConsultationSession> {

    // 查询用户的会话列表
    List<AiConsultationSession> selectByUserId(@Param("userId") Long userId);

    // 查询用户进行中的会话
    AiConsultationSession selectActiveByUserIdAndType(@Param("userId") Long userId, @Param("sessionType") String sessionType);
}
