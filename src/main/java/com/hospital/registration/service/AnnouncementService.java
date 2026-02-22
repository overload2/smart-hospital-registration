package com.hospital.registration.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.dto.AnnouncementDTO;
import com.hospital.registration.vo.AnnouncementVO;

import java.util.List;

/**
 * @title: AnnouncementService
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告服务接口
 */
public interface AnnouncementService {

    /**
     * 分页查询公告列表
     */
    Page<AnnouncementVO> getAnnouncementPage(Integer pageNum, Integer pageSize, String title, Integer status);

    /**
     * 根据ID查询公告
     */
    AnnouncementVO getAnnouncementById(Long id);

    /**
     * 新增公告
     */
    AnnouncementVO createAnnouncement(AnnouncementDTO dto);

    /**
     * 修改公告
     */
    AnnouncementVO updateAnnouncement(Long id, AnnouncementDTO dto);

    /**
     * 删除公告
     */
    void deleteAnnouncement(Long id);

    /**
     * 修改公告状态
     */
    void updateStatus(Long id, Integer status);

    /**
     * 获取已发布的公告列表（患者端首页）
     */
    List<AnnouncementVO> getPublishedList(Integer limit);
}

