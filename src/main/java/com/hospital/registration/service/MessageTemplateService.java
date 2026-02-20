package com.hospital.registration.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.dto.MessageTemplateDTO;
import com.hospital.registration.vo.MessageTemplateVO;

import java.util.List;

/**
 * @title: MessageTemplateService
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板服务接口
 */
public interface MessageTemplateService {

    /**
     * 新增消息模板
     *
     * @param dto 模板DTO
     * @return 模板VO
     */
    MessageTemplateVO addTemplate(MessageTemplateDTO dto);

    /**
     * 更新消息模板
     *
     * @param id  模板ID
     * @param dto 模板DTO
     * @return 模板VO
     */
    MessageTemplateVO updateTemplate(Long id, MessageTemplateDTO dto);

    /**
     * 删除消息模板
     *
     * @param id 模板ID
     */
    void deleteTemplate(Long id);

    /**
     * 根据ID查询模板
     *
     * @param id 模板ID
     * @return 模板VO
     */
    MessageTemplateVO getTemplateById(Long id);

    /**
     * 分页查询消息模板
     *
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @param keyword     关键词
     * @param messageType 消息类型
     * @param status      状态
     * @return 分页结果
     */
    IPage<MessageTemplateVO> getTemplatePage(Integer pageNum, Integer pageSize,
                                             String keyword, String messageType, Integer status);

    /**
     * 查询所有启用的模板
     *
     * @return 模板列表
     */
    List<MessageTemplateVO> getActiveTemplates();

    /**
     * 更新模板状态
     *
     * @param id     模板ID
     * @param status 状态
     */
    void updateTemplateStatus(Long id, Integer status);
    /**
     * 批量更新模板状态
     *
     * @param ids    模板ID列表
     * @param status 状态
     */
    void batchUpdateStatus(List<Long> ids, Integer status);
}