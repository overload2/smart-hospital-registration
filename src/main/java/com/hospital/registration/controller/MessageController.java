package com.hospital.registration.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.vo.MessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
    @OperationLog(module = "消息管理", operation = "UPDATE")
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
    @OperationLog(module = "消息管理", operation = "UPDATE")
    public Result markAllAsRead(@RequestParam Long userId) {
        log.info("全部标记已读 - 用户ID: {}", userId);
        messageService.markAllAsRead(userId);
        return Result.ok("全部标记已读成功");
    }

    /**
     * 分页查询消息记录（管理端）
     */
    @GetMapping("/page")
    @RequirePermission("message:list")
    public Result getMessagePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) Long userId,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) String channel,
            @RequestParam(required = false) Integer sendStatus,
            @RequestParam(required = false) Integer readStatus) {
        log.info("分页查询消息记录 - 页码: {}, 每页大小: {}", pageNum, pageSize);
        IPage<MessageRecordVO> page = messageService.getMessagePage(
                pageNum, pageSize, userId, messageType, channel, sendStatus, readStatus);
        return Result.ok().data("page", page);
    }

    /**
     * 发送系统公告
     */
    @PostMapping("/send-announcement")
    @RequirePermission("message:send")
    @OperationLog(module = "消息管理", operation = "ADD")
    public Result sendAnnouncement(@RequestBody Map<String, Object> params) {
        String title = (String) params.get("title");
        String content = (String) params.get("content");
        log.info("发送系统公告 - 标题: {}", title);
        messageService.sendSystemAnnouncement(title, content);
        return Result.ok("系统公告发送成功");
    }

    /**
     * 重发失败消息
     */
    @PostMapping("/{id}/resend")
    @RequirePermission("message:send")
    @OperationLog(module = "消息管理", operation = "UPDATE")
    public Result resendMessage(@PathVariable Long id) {
        log.info("重发消息 - 消息ID: {}", id);
        messageService.resendMessage(id);
        return Result.ok("消息重发成功");
    }

    /**
     * 删除消息记录
     */
    @PostMapping("/delete/{id}")
    @RequirePermission("message:delete")
    @OperationLog(module = "消息管理", operation = "DELETE")
    public Result deleteMessage(@PathVariable Long id) {
        log.info("删除消息 - 消息ID: {}", id);
        messageService.deleteMessage(id);
        return Result.ok("消息删除成功");
    }

    /**
     * 批量删除消息记录
     */
    @PostMapping("/batch-delete")
    @RequirePermission("message:delete")
    @OperationLog(module = "消息管理", operation = "DELETE")
    public Result batchDeleteMessage(@RequestBody Map<String, Object> params) {
        List<Integer> idList = (List<Integer>) params.get("ids");
        List<Long> ids = new ArrayList<>();
        for (Integer id : idList) {
            ids.add(id.longValue());
        }
        log.info("批量删除消息 - ids: {}", ids);
        messageService.batchDeleteMessage(ids);
        return Result.ok("批量删除成功");
    }
}

