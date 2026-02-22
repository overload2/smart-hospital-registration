package com.hospital.registration.controller.app;

import com.hospital.registration.common.Result;
import com.hospital.registration.service.AnnouncementService;
import com.hospital.registration.vo.AnnouncementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @title: AppAnnouncementController
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 患者端公告控制器
 */
@Slf4j
@RestController
@RequestMapping("/app/announcement")
public class AppAnnouncementController {

    private final AnnouncementService announcementService;

    public AppAnnouncementController(AnnouncementService announcementService) {
        this.announcementService = announcementService;
    }

    /**
     * 获取首页公告列表
     */
    @GetMapping("/list")
    public Result list(@RequestParam(defaultValue = "5") Integer limit) {
        log.info("获取首页公告列表 - limit: {}", limit);
        List<AnnouncementVO> list = announcementService.getPublishedList(limit);
        return Result.ok().data("announcements", list);
    }
}
