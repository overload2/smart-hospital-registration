package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.dto.AiChatDTO;
import com.hospital.registration.service.AiConsultationService;
import com.hospital.registration.vo.AiChatVO;
import com.hospital.registration.vo.AiSessionVO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: AppAiConsultationController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端AI问诊控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/ai")
public class AppAiConsultationController {

    private final AiConsultationService aiConsultationService;

    /**
     * 构造器注入
     */
    public AppAiConsultationController(AiConsultationService aiConsultationService) {
        this.aiConsultationService = aiConsultationService;
    }

    /**
     * AI对话
     */
    @PostMapping("/chat")
    public Result chat(@Valid @RequestBody AiChatDTO dto, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("患者端AI对话 - 用户ID: {}, 会话类型: {}", userId, dto.getSessionType());
        AiChatVO vo = aiConsultationService.chat(userId, dto);
        return Result.ok().data("chat", vo);
    }

    /**
     * 获取我的会话列表
     */
    @GetMapping("/sessions")
    public Result getSessions(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取AI会话列表 - 用户ID: {}", userId);
        List<AiSessionVO> sessions = aiConsultationService.getUserSessions(userId);
        return Result.ok().data("sessions", sessions);
    }

    /**
     * 获取会话详情
     */
    @GetMapping("/sessions/{sessionId}")
    public Result getSessionDetail(@PathVariable Long sessionId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取AI会话详情 - 会话ID: {}, 用户ID: {}", sessionId, userId);
        AiSessionVO session = aiConsultationService.getSessionDetail(sessionId, userId);
        return Result.ok().data("session", session);
    }

    /**
     * 结束会话
     */
    @PostMapping("/sessions/{sessionId}/end")
    public Result endSession(@PathVariable Long sessionId, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("结束AI会话 - 会话ID: {}, 用户ID: {}", sessionId, userId);
        aiConsultationService.endSession(sessionId, userId);
        return Result.ok("会话已结束");
    }
}
