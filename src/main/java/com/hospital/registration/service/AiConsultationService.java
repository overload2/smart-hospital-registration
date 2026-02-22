package com.hospital.registration.service;

import com.hospital.registration.dto.AiChatDTO;
import com.hospital.registration.vo.AiChatVO;
import com.hospital.registration.vo.AiSessionVO;

import java.util.List;

/**
 * @title AI问诊服务接口
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI问诊业务接口
 */
public interface AiConsultationService {

    /**
     * AI对话
     *
     * @param userId 用户ID
     * @param dto 对话请求
     * @return 对话响应
     */
    AiChatVO chat(Long userId, AiChatDTO dto);

    /**
     * 获取用户的会话列表
     *
     * @param userId 用户ID
     * @return 会话列表
     */
    List<AiSessionVO> getUserSessions(Long userId);

    /**
     * 获取会话详情（包含消息）
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     * @return 会话详情
     */
    AiSessionVO getSessionDetail(Long sessionId, Long userId);

    /**
     * 结束会话
     *
     * @param sessionId 会话ID
     * @param userId 用户ID
     */
    void endSession(Long sessionId, Long userId);
}
