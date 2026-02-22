package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.MessageRecord;
import com.hospital.registration.vo.MessageRecordVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: MessageRecordMapper
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息记录Mapper
 */
@Mapper
public interface MessageRecordMapper extends BaseMapper<MessageRecord> {

    /**
     * 分页查询用户消息列表
     *
     * @param page   分页对象
     * @param userId 用户ID
     * @return 消息VO分页对象
     */
    IPage<MessageRecordVO> selectPageByUserId(Page<MessageRecordVO> page, @Param("userId") Long userId);

    /**
     * 统计用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Integer countUnread(@Param("userId") Long userId);

    /**
     * 分页查询消息记录（管理端）
     */
    IPage<MessageRecordVO> selectMessagePage(Page<MessageRecordVO> page,
                                             @Param("userId") Long userId,
                                             @Param("messageType") String messageType,
                                             @Param("channel") String channel,
                                             @Param("sendStatus") Integer sendStatus,
                                             @Param("readStatus") Integer readStatus);

    /**
     * 批量删除消息记录
     */
    void batchDelete(@Param("ids") List<Long> ids);
}

