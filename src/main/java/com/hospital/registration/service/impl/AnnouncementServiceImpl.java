package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.AnnouncementDTO;
import com.hospital.registration.entity.Announcement;
import com.hospital.registration.mapper.AnnouncementMapper;
import com.hospital.registration.service.AnnouncementService;
import com.hospital.registration.vo.AnnouncementVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: AnnouncementServiceImpl
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告服务实现类
 */
@Slf4j
@Service
public class AnnouncementServiceImpl implements AnnouncementService {

    private final AnnouncementMapper announcementMapper;

    public AnnouncementServiceImpl(AnnouncementMapper announcementMapper) {
        this.announcementMapper = announcementMapper;
    }

    /**
     * 分页查询公告列表
     */
    @Override
    public Page<AnnouncementVO> getAnnouncementPage(Integer pageNum, Integer pageSize, String title, Integer status) {
        log.info("分页查询公告列表 - pageNum: {}, pageSize: {}", pageNum, pageSize);
        Page<AnnouncementVO> page = new Page<>(pageNum, pageSize);
        Page<AnnouncementVO> result = announcementMapper.selectPageList(page, title, status);
        // 填充状态名称
        for (AnnouncementVO vo : result.getRecords()) {
            vo.setStatusName(vo.getStatus() == 1 ? "已发布" : "已下架");
        }
        return result;
    }

    /**
     * 根据ID查询公告
     */
    @Override
    public AnnouncementVO getAnnouncementById(Long id) {
        log.info("查询公告详情 - ID: {}", id);
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在");
        }
        AnnouncementVO vo = new AnnouncementVO();
        BeanUtils.copyProperties(announcement, vo);
        vo.setStatusName(vo.getStatus() == 1 ? "已发布" : "已下架");
        return vo;
    }

    /**
     * 新增公告
     */
    @Override
    public AnnouncementVO createAnnouncement(AnnouncementDTO dto) {
        log.info("新增公告 - 标题: {}", dto.getTitle());
        Announcement announcement = new Announcement();
        BeanUtils.copyProperties(dto, announcement);
        if (announcement.getStatus() == null) {
            announcement.setStatus(1);
        }
        announcementMapper.insert(announcement);
        log.info("新增公告成功 - ID: {}", announcement.getId());
        return getAnnouncementById(announcement.getId());
    }

    /**
     * 修改公告
     */
    @Override
    public AnnouncementVO updateAnnouncement(Long id, AnnouncementDTO dto) {
        log.info("修改公告 - ID: {}", id);
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在");
        }
        Announcement updateAnnouncement = new Announcement();
        updateAnnouncement.setId(id);
        updateAnnouncement.setTitle(dto.getTitle());
        updateAnnouncement.setContent(dto.getContent());
        if (dto.getStatus() != null) {
            updateAnnouncement.setStatus(dto.getStatus());
        }
        announcementMapper.updateById(updateAnnouncement);
        log.info("修改公告成功 - ID: {}", id);
        return getAnnouncementById(id);
    }

    /**
     * 删除公告
     */
    @Override
    public void deleteAnnouncement(Long id) {
        log.info("删除公告 - ID: {}", id);
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在");
        }
        announcementMapper.deleteById(id);
        log.info("删除公告成功 - ID: {}", id);
    }

    /**
     * 修改公告状态
     */
    @Override
    public void updateStatus(Long id, Integer status) {
        log.info("修改公告状态 - ID: {}, status: {}", id, status);
        Announcement announcement = announcementMapper.selectById(id);
        if (announcement == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "公告不存在");
        }
        Announcement updateAnnouncement = new Announcement();
        updateAnnouncement.setId(id);
        updateAnnouncement.setStatus(status);
        announcementMapper.updateById(updateAnnouncement);
        log.info("修改公告状态成功 - ID: {}", id);
    }

    /**
     * 获取已发布的公告列表（患者端首页）
     */
    @Override
    public List<AnnouncementVO> getPublishedList(Integer limit) {
        log.info("获取已发布公告列表 - limit: {}", limit);
        return announcementMapper.selectPublishedList(limit);
    }
}
