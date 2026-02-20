package com.hospital.registration.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.MessageTemplateDTO;
import com.hospital.registration.service.MessageTemplateService;
import com.hospital.registration.vo.MessageTemplateVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @title: MessageTemplateController
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板控制器
 */
@Slf4j
@RestController
@RequestMapping("/message-template")
public class MessageTemplateController {

    private final MessageTemplateService messageTemplateService;

    /**
     * 构造器注入
     */
    public MessageTemplateController(MessageTemplateService messageTemplateService) {
        this.messageTemplateService = messageTemplateService;
    }

    /**
     * 新增消息模板
     */
    @PostMapping("/add")
    @RequirePermission("message:template:add")
    @OperationLog(module = "消息模板管理", operation = "ADD")
    public Result addTemplate(@Valid @RequestBody MessageTemplateDTO dto) {
        MessageTemplateVO vo = messageTemplateService.addTemplate(dto);
        return Result.ok("模板新增成功").data("template", vo);
    }

    /**
     * 更新消息模板
     */
    @PutMapping("/{id}")
    @RequirePermission("message:template:edit")
    @OperationLog(module = "消息模板管理", operation = "UPDATE")
    public Result updateTemplate(@PathVariable Long id,
                                 @Valid @RequestBody MessageTemplateDTO dto) {
        MessageTemplateVO vo = messageTemplateService.updateTemplate(id, dto);
        return Result.ok("模板更新成功").data("template", vo);
    }

    /**
     * 删除消息模板
     */
    @DeleteMapping("/{id}")
    @RequirePermission("message:template:delete")
    @OperationLog(module = "消息模板管理", operation = "DELETE")
    public Result deleteTemplate(@PathVariable Long id) {
        messageTemplateService.deleteTemplate(id);
        return Result.ok("模板删除成功");
    }

    /**
     * 根据ID查询模板
     */
    @GetMapping("/{id}")
    public Result getTemplateById(@PathVariable Long id) {
        MessageTemplateVO vo = messageTemplateService.getTemplateById(id);
        return Result.ok().data("template", vo);
    }

    /**
     * 分页查询消息模板
     */
    @GetMapping("/page")
    public Result getTemplatePage(
            @RequestParam(defaultValue = "1") Integer pageNum,
            @RequestParam(defaultValue = "10") Integer pageSize,
            @RequestParam(required = false) String keyword,
            @RequestParam(required = false) String messageType,
            @RequestParam(required = false) Integer status) {
        IPage<MessageTemplateVO> page = messageTemplateService.getTemplatePage(
                pageNum, pageSize, keyword, messageType, status);
        return Result.ok().data("page", page);
    }

    /**
     * 查询所有启用的模板
     */
    @GetMapping("/active")
    public Result getActiveTemplates() {
        List<MessageTemplateVO> list = messageTemplateService.getActiveTemplates();
        return Result.ok().data("templates", list);
    }

    /**
     * 更新模板状态
     */
    @PostMapping("/{id}/status")
    @RequirePermission("message:template:edit")
    @OperationLog(module = "消息模板管理", operation = "UPDATE")
    public Result updateTemplateStatus(@PathVariable Long id,
                                       @RequestParam Integer status) {
        messageTemplateService.updateTemplateStatus(id, status);
        return Result.ok("状态更新成功");
    }
    /**
     * 批量更新模板状态
     */
    @PostMapping("/batch-status")
    @RequirePermission("message:template:edit")
    @OperationLog(module = "消息模板管理", operation = "UPDATE")
    public Result batchUpdateStatus(@RequestBody Map<String, Object> params) {
        List<Integer> idList = (List<Integer>) params.get("ids");
        Integer status = (Integer) params.get("status");

        log.info("批量更新模板状态 - ids: {}, status: {}", idList, status);

        List<Long> ids = new ArrayList<>();
        for (Integer id : idList) {
            ids.add(id.longValue());
        }
        messageTemplateService.batchUpdateStatus(ids, status);

        return Result.ok("批量更新状态成功");
    }
}
