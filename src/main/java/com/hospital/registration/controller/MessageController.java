package com.hospital.registration.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.common.Result;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.vo.MessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @title: MessageController
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息通知控制器
 */
@Slf4j
@RestController
@RequestMapping("/message")
public class MessageController {

    private final MessageService messageService;

    /**
     * 构造器注入
     */
    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    /**
     * 分页查询用户消息列表
     *
     * @param userId   用户ID
     * @param pageNum  页码（默认1）
     * @param pageSize 每页大小（默认10）
     * @return 消息VO分页对象
     */
    @GetMapping("/list")
    public Result list(
            @RequestParam Long userId,
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize) {
        log.info("查询消息列表 - 用户ID: {}, 页码: {}, 每页大小: {}", userId, pageNum, pageSize);
        IPage<MessageRecordVO> page = messageService.getUserMessages(userId, pageNum, pageSize);
        return Result.ok()
                .data("records", page.getRecords())
                .data("total", page.getTotal())
                .data("pages", page.getPages())
                .data("current", page.getCurrent());
    }

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    @GetMapping("/unread-count")
    public Result unreadCount(@RequestParam Long userId) {
        log.info("获取未读消息数量 - 用户ID: {}", userId);
        Integer count = messageService.getUnreadCount(userId);
        return Result.ok().data("count", count);
    }

    /**
     * 标记单条消息已读
     *
     * @param id     消息ID
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/{id}/read")
    public Result markAsRead(@PathVariable Long id, @RequestParam Long userId) {
        log.info("标记消息已读 - 消息ID: {}, 用户ID: {}", id, userId);
        messageService.markAsRead(id, userId);
        return Result.ok("标记已读成功");
    }

    /**
     * 全部标记已读
     *
     * @param userId 用户ID
     * @return 操作结果
     */
    @PostMapping("/read-all")
    public Result markAllAsRead(@RequestParam Long userId) {
        log.info("全部标记已读 - 用户ID: {}", userId);
        messageService.markAllAsRead(userId);
        return Result.ok("全部标记已读成功");
    }
}

