package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.MessageChannel;
import com.hospital.registration.common.MessageType;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.MessageTemplateDTO;
import com.hospital.registration.entity.MessageTemplate;
import com.hospital.registration.mapper.MessageTemplateMapper;
import com.hospital.registration.service.MessageTemplateService;
import com.hospital.registration.vo.MessageTemplateVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @title: MessageTemplateServiceImpl
 * @author: Su
 * @date: 2026/2/19
 * @version: 1.0
 * @description: 消息模板服务实现类
 */
@Slf4j
@Service
public class MessageTemplateServiceImpl implements MessageTemplateService {

    private final MessageTemplateMapper messageTemplateMapper;

    /**
     * 构造器注入
     */
    public MessageTemplateServiceImpl(MessageTemplateMapper messageTemplateMapper) {
        this.messageTemplateMapper = messageTemplateMapper;
    }

    /**
     * 新增消息模板
     */
    @Override
    public MessageTemplateVO addTemplate(MessageTemplateDTO dto) {
        log.info("新增消息模板 - 模板编码: {}", dto.getTemplateCode());

        // 检查模板编码是否已存在
        MessageTemplate exist = messageTemplateMapper.selectByTemplateCode(dto.getTemplateCode());
        if (exist != null) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "模板编码已存在");
        }

        MessageTemplate template = new MessageTemplate();
        BeanUtils.copyProperties(dto, template);

        messageTemplateMapper.insert(template);
        log.info("消息模板新增成功 - ID: {}", template.getId());

        return convertToVO(template);
    }

    /**
     * 更新消息模板
     */
    @Override
    public MessageTemplateVO updateTemplate(Long id, MessageTemplateDTO dto) {
        log.info("更新消息模板 - ID: {}", id);

        MessageTemplate template = messageTemplateMapper.selectById(id);
        if (template == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "模板不存在");
        }

        // 如果修改了模板编码，检查是否重复
        if (!template.getTemplateCode().equals(dto.getTemplateCode())) {
            MessageTemplate exist = messageTemplateMapper.selectByTemplateCode(dto.getTemplateCode());
            if (exist != null) {
                throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "模板编码已存在");
            }
        }

        BeanUtils.copyProperties(dto, template);
        template.setId(id);

        messageTemplateMapper.updateById(template);
        log.info("消息模板更新成功 - ID: {}", id);

        return convertToVO(template);
    }

    /**
     * 删除消息模板
     */
    @Override
    public void deleteTemplate(Long id) {
        log.info("删除消息模板 - ID: {}", id);

        MessageTemplate template = messageTemplateMapper.selectById(id);
        if (template == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "模板不存在");
        }

        messageTemplateMapper.deleteById(id);
        log.info("消息模板删除成功 - ID: {}", id);
    }

    /**
     * 根据ID查询模板
     */
    @Override
    public MessageTemplateVO getTemplateById(Long id) {
        MessageTemplate template = messageTemplateMapper.selectById(id);
        if (template == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "模板不存在");
        }
        return convertToVO(template);
    }

    /**
     * 分页查询消息模板
     */
    @Override
    public IPage<MessageTemplateVO> getTemplatePage(Integer pageNum, Integer pageSize,
                                                    String keyword, String messageType, Integer status) {
        log.info("分页查询消息模板 - 页码: {}, 每页大小: {}", pageNum, pageSize);

        Page<MessageTemplateVO> page = new Page<>(pageNum, pageSize);
        IPage<MessageTemplateVO> result = messageTemplateMapper.selectTemplatePage(page, keyword, messageType, status);

        // 填充名称字段
        result.getRecords().forEach(this::enrichTemplateVO);

        return result;
    }

    /**
     * 查询所有启用的模板
     */
    @Override
    public List<MessageTemplateVO> getActiveTemplates() {
        List<MessageTemplateVO> list = messageTemplateMapper.selectActiveTemplates();
        list.forEach(this::enrichTemplateVO);
        return list;
    }

    /**
     * 更新模板状态
     */
    @Override
    public void updateTemplateStatus(Long id, Integer status) {
        log.info("更新模板状态 - ID: {}, 状态: {}", id, status);

        MessageTemplate template = messageTemplateMapper.selectById(id);
        if (template == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "模板不存在");
        }

        template.setStatus(status);
        messageTemplateMapper.updateById(template);
    }
    /**
     * 批量更新模板状态
     */
    @Override
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        log.info("批量更新模板状态 - ids: {}, status: {}", ids, status);
        messageTemplateMapper.batchUpdateStatus(ids, status);
    }

    /**
     * Entity转VO
     */
    private MessageTemplateVO convertToVO(MessageTemplate template) {
        MessageTemplateVO vo = new MessageTemplateVO();
        BeanUtils.copyProperties(template, vo);
        enrichTemplateVO(vo);
        return vo;
    }

    /**
     * 丰富VO信息（添加名称字段）
     */
    private void enrichTemplateVO(MessageTemplateVO vo) {
        // 消息类型名称
        try {
            vo.setMessageTypeName(MessageType.valueOf(vo.getMessageType()).getName());
        } catch (Exception e) {
            vo.setMessageTypeName(vo.getMessageType());
        }

        // 渠道名称
        try {
            vo.setChannelName(MessageChannel.valueOf(vo.getChannel()).getName());
        } catch (Exception e) {
            vo.setChannelName(vo.getChannel());
        }
    }

}
