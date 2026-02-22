package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.MessageTemplate;
import com.hospital.registration.vo.MessageTemplateVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @title: MessageTemplateMapper
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板Mapper
 */
@Mapper
public interface MessageTemplateMapper extends BaseMapper<MessageTemplate> {

    /**
     * 分页查询消息模板
     */
    IPage<MessageTemplateVO> selectTemplatePage(Page<MessageTemplateVO> page,
                                                @Param("keyword") String keyword,
                                                @Param("messageType") String messageType,
                                                @Param("status") Integer status);

    /**
     * 查询所有启用的模板
     */
    List<MessageTemplateVO> selectActiveTemplates();

    /**
     * 根据模板编码查询
     */
    MessageTemplate selectByTemplateCode(@Param("templateCode") String templateCode);

    /**
     * 批量更新模板状态
     */
    void batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);
}
