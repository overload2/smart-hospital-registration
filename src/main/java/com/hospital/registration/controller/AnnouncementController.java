package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.AnnouncementDTO;
import com.hospital.registration.service.AnnouncementService;
import com.hospital.registration.vo.AnnouncementVO;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * @title: AnnouncementController
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告管理控制器（管理端）
 */
@Slf4j
@RestController
@RequestMapping("/announcement")
public class AnnouncementController {

    private final AnnouncementService announcementService;

    public AnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * 分页查询公告列表
     */
    @GetMapping("/page")
    public Result page(@RequestParam(defaultValue = "1") Integer pageNum,
                       @RequestParam(defaultValue = "10") Integer pageSize,
                       @RequestParam(required = false) String title,
                       @RequestParam(required = false) Integer status) {
        log.info("分页查询公告列表");
        Page<AnnouncementVO> page = announcementService.getAnnouncementPage(pageNum, pageSize, title, status);
        return Result.ok()
                .data("records", page.getRecords())
                .data("total", page.getTotal());
    }

    /**
     * 查询公告详情
     */
    @GetMapping("/{id}")
    public Result detail(@PathVariable Long id) {
        log.info("查询公告详情 - ID: {}", id);
        AnnouncementVO vo = announcementService.getAnnouncementById(id);
        return Result.ok().data("announcement", vo);
    }

    /**
     * 新增公告
     */
    @PostMapping
    public Result create(@Valid @RequestBody AnnouncementDTO dto) {
        log.info("新增公告 - 标题: {}", dto.getTitle());
        AnnouncementVO vo = announcementService.createAnnouncement(dto);
        return Result.ok("新增成功").data("announcement", vo);
    }

    /**
     * 修改公告
     */
    @PutMapping("/{id}")
    public Result update(@PathVariable Long id, @Valid @RequestBody AnnouncementDTO dto) {
        log.info("修改公告 - ID: {}", id);
        AnnouncementVO vo = announcementService.updateAnnouncement(id, dto);
        return Result.ok("修改成功").data("announcement", vo);
    }

    /**
     * 删除公告
     */
    @DeleteMapping("/{id}")
    public Result delete(@PathVariable Long id) {
        log.info("删除公告 - ID: {}", id);
        announcementService.deleteAnnouncement(id);
        return Result.ok("删除成功");
    }

    /**
     * 修改公告状态（发布/下架）
     */
    @PutMapping("/{id}/status")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("修改公告状态 - ID: {}, status: {}", id, status);
        announcementService.updateStatus(id, status);
        return Result.ok(status == 1 ? "发布成功" : "下架成功");
    }
}
