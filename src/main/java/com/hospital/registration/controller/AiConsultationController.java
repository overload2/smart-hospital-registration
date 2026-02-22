package com.hospital.registration.controller;

import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Constants;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.AiChatDTO;
import com.hospital.registration.service.AiConsultationService;
import com.hospital.registration.utils.JwtUtil;
import com.hospital.registration.vo.AiChatVO;
import com.hospital.registration.vo.AiSessionVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title AI问诊Controller
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI智能问诊接口
 */
@Slf4j
@RestController
@RequestMapping("/ai")
public class AiConsultationController {

    private final AiConsultationService aiConsultationService;
    private final JwtUtil jwtUtil;

    public AiConsultationController(AiConsultationService aiConsultationService, JwtUtil jwtUtil) {
        this.aiConsultationService = aiConsultationService;
        this.jwtUtil = jwtUtil;
    }

    /**
     * AI对话
     */
    @PostMapping("/chat")
    @OperationLog(module = "AI问诊", operation = "ADD")
    public Result chat(@Valid @RequestBody AiChatDTO dto,
                       @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace(Constants.Jwt.PREFIX, "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的token");
        }

        log.info("AI对话 - 用户ID: {}", userId);
        AiChatVO vo = aiConsultationService.chat(userId, dto);
        return Result.ok().data("chat", vo);
    }

    /**
     * 获取我的会话列表
     */
    @GetMapping("/sessions")
    public Result getSessions(@RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace(Constants.Jwt.PREFIX, "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的token");
        }

        log.info("获取会话列表 - 用户ID: {}", userId);
        List<AiSessionVO> sessions = aiConsultationService.getUserSessions(userId);
        return Result.ok().data("sessions", sessions);
    }

    /**
     * 获取会话详情
     */
    @GetMapping("/sessions/{sessionId}")
    public Result getSessionDetail(@PathVariable Long sessionId,
                                   @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace(Constants.Jwt.PREFIX, "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的token");
        }

        log.info("获取会话详情 - 会话ID: {}, 用户ID: {}", sessionId, userId);
        AiSessionVO session = aiConsultationService.getSessionDetail(sessionId, userId);
        return Result.ok().data("session", session);
    }

    /**
     * 结束会话
     */
    @PostMapping("/sessions/{sessionId}/end")
    @OperationLog(module = "AI问诊", operation = "UPDATE")
    public Result endSession(@PathVariable Long sessionId,
                             @RequestHeader("Authorization") String authHeader) {
        String token = authHeader.replace(Constants.Jwt.PREFIX, "");
        Long userId = jwtUtil.getUserIdFromToken(token);
        if (userId == null) {
            return Result.error("无效的token");
        }

        log.info("结束会话 - 会话ID: {}, 用户ID: {}", sessionId, userId);
        aiConsultationService.endSession(sessionId, userId);
        return Result.ok("会话已结束");
    }
}
