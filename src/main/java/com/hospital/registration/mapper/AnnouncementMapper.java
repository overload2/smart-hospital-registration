package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.Announcement;
import com.hospital.registration.vo.AnnouncementVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: AnnouncementMapper
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 公告Mapper
 */
@Mapper
public interface AnnouncementMapper extends BaseMapper<Announcement> {

    /**
     * 分页查询公告列表
     */
    Page<AnnouncementVO> selectPageList(Page<AnnouncementVO> page,
                                        @Param("title") String title,
                                        @Param("status") Integer status);

    /**
     * 查询已发布的公告列表（患者端首页）
     */
    List<AnnouncementVO> selectPublishedList(@Param("limit") Integer limit);
}
