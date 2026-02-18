package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.AiConsultationMessage;
import com.hospital.registration.vo.AiMessageVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title AI问诊消息Mapper
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊消息数据访问接口
 */
@Mapper
public interface AiConsultationMessageMapper extends BaseMapper<AiConsultationMessage> {

    // 查询会话的消息列表（包含科室和医生名称）
    List<AiMessageVO> selectBySessionId(@Param("sessionId") Long sessionId);

    // 查询会话的消息列表（用于构建上下文）
    List<AiConsultationMessage> selectMessagesBySessionId(@Param("sessionId") Long sessionId);
}
