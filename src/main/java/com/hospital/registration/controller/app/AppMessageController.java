package com.hospital.registration.controller.app;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.common.Result;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.vo.MessageRecordVO;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @title: AppMessageController
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端消息控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/message")
public class AppMessageController {

    private final MessageService messageService;

    /**
     * 构造器注入
     */
    public AppMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 获取我的消息列表
     */
    @GetMapping("/my-list")
    public Result myList(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "20") Integer pageSize,
            HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取我的消息列表 - 用户ID: {}", userId);
        IPage<MessageRecordVO> page = messageService.getUserMessages(userId, pageNum, pageSize);
        return Result.ok().data("messages", page.getRecords()).data("total", page.getTotal());
    }

    /**
     * 获取未读消息数量
     */
    @GetMapping("/unread-count")
    public Result unreadCount(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("获取未读消息数量 - 用户ID: {}", userId);
        Integer count = messageService.getUnreadCount(userId);
        return Result.ok().data("count", count);
    }

    /**
     * 标记消息已读
     */
    @PostMapping("/read/{id}")
    public Result read(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("标记消息已读 - 消息ID: {}, 用户ID: {}", id, userId);
        messageService.markAsRead(id, userId);
        return Result.ok("已读");
    }

    /**
     * 全部标记已读
     */
    @PostMapping("/read-all")
    public Result readAll(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        log.info("全部标记已读 - 用户ID: {}", userId);
        messageService.markAllAsRead(userId);
        return Result.ok("全部已读");
    }
}
