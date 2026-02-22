package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.*;
import com.hospital.registration.entity.*;
import com.hospital.registration.mapper.MessageRecordMapper;
import com.hospital.registration.mapper.MessageTemplateMapper;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.vo.MessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @title: MessageServiceImpl
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息服务实现类
 */
@Slf4j
@Service
public class MessageServiceImpl implements MessageService {

    private final MessageRecordMapper messageRecordMapper;
    private final UserMapper userMapper;
    private final MessageTemplateMapper messageTemplateMapper;

    // 医院名称（用于消息前缀）
    private static final String HOSPITAL_NAME = "智慧医院";

    /**
     * 构造器注入
     */
    public MessageServiceImpl(MessageRecordMapper messageRecordMapper, UserMapper userMapper,
             MessageTemplateMapper messageTemplateMapper) {
        this.messageRecordMapper = messageRecordMapper;
        this.userMapper = userMapper;
        this.messageTemplateMapper = messageTemplateMapper;
    }

    /**
     * 发送挂号成功通知
     */
    @Override
    public void sendRegistrationSuccessNotice(Registration registration, String departmentName, String doctorName) {
        log.info("发送挂号成功通知 - 挂号单号: {}", registration.getRegistrationNo());

        User user = userMapper.selectById(registration.getPatientId());
        if (user == null) {
            log.warn("发送挂号成功通知失败：用户不存在 - 用户ID: {}", registration.getPatientId());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("registrationNo", registration.getRegistrationNo());
        params.put("visitDate", registration.getRegistrationDate().toString());
        params.put("timeSlot", getTimeSlotName(registration.getTimeSlot().getTimeRange()));
        params.put("departmentName", departmentName);
        params.put("doctorName", doctorName);
        params.put("queueNumber", String.valueOf(registration.getQueueNumber()));
        params.put("fee", String.format("%.2f", registration.getRegistrationFee()));

        sendMessageByTemplate(user.getId(), user.getPhone(), "REG_SUCCESS", params);
    }


    /**
     * 发送支付成功通知
     */
    @Override
    public void sendPaymentSuccessNotice(Payment payment, Registration registration) {
        log.info("发送支付成功通知 - 交易流水号: {}", payment.getTransactionNo());

        User user = userMapper.selectById(payment.getUserId());
        if (user == null) {
            log.warn("发送支付成功通知失败：用户不存在 - 用户ID: {}", payment.getUserId());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("amount", String.format("%.2f", payment.getAmount()));
        params.put("paymentMethod", getPaymentMethodName(payment.getPaymentMethod()));
        params.put("registrationNo", registration.getRegistrationNo());

        sendMessageByTemplate(user.getId(), user.getPhone(), "PAY_SUCCESS", params);
    }

    /**
     * 发送挂号取消通知
     */
    @Override
    public void sendRegistrationCancelNotice(Registration registration, String departmentName, String doctorName) {
        log.info("发送挂号取消通知 - 挂号单号: {}", registration.getRegistrationNo());

        User user = userMapper.selectById(registration.getPatientId());
        if (user == null) {
            log.warn("发送挂号取消通知失败：用户不存在 - 用户ID: {}", registration.getPatientId());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("registrationNo", registration.getRegistrationNo());
        params.put("visitDate", registration.getRegistrationDate().toString());
        params.put("departmentName", departmentName);
        params.put("doctorName", doctorName);

        sendMessageByTemplate(user.getId(), user.getPhone(), "REG_CANCEL", params);
    }

    /**
     * 发送退款成功通知
     */
    @Override
    public void sendRefundSuccessNotice(Payment payment) {
        log.info("发送退款成功通知 - 交易流水号: {}", payment.getTransactionNo());

        User user = userMapper.selectById(payment.getUserId());
        if (user == null) {
            log.warn("发送退款成功通知失败：用户不存在 - 用户ID: {}", payment.getUserId());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("amount", String.format("%.2f", payment.getAmount()));
        params.put("transactionNo", payment.getTransactionNo());

        sendMessageByTemplate(user.getId(), user.getPhone(), "REFUND_SUCCESS", params);
    }

    /**
     * 分页查询用户消息列表
     */
    @Override
    public IPage<MessageRecordVO> getUserMessages(Long userId, Integer pageNum, Integer pageSize) {
        log.info("查询用户消息列表 - 用户ID: {}, 页码: {}, 每页大小: {}", userId, pageNum, pageSize);

        Page<MessageRecordVO> page = new Page<>(pageNum, pageSize);
        IPage<MessageRecordVO> result = messageRecordMapper.selectPageByUserId(page, userId);

        // 填充名称字段
        result.getRecords().forEach(this::enrichMessageRecordVO);

        log.info("查询到 {} 条消息记录", result.getRecords().size());
        return result;
    }

    /**
     * 获取用户未读消息数量
     */
    @Override
    public Integer getUnreadCount(Long userId) {
        log.info("获取未读消息数量 - 用户ID: {}", userId);
        return messageRecordMapper.countUnread(userId);
    }

    /**
     * 标记消息已读
     */
    @Override
    public void markAsRead(Long messageId, Long userId) {
        log.info("标记消息已读 - 消息ID: {}, 用户ID: {}", messageId, userId);

        LambdaUpdateWrapper<MessageRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MessageRecord::getId, messageId)
                .eq(MessageRecord::getUserId, userId)
                .eq(MessageRecord::getReadStatus, 0)
                .set(MessageRecord::getReadStatus, 1)
                .set(MessageRecord::getReadTime, LocalDateTime.now());

        messageRecordMapper.update(null, wrapper);
    }

    /**
     * 全部标记已读
     */
    @Override
    public void markAllAsRead(Long userId) {
        log.info("全部标记已读 - 用户ID: {}", userId);

        LambdaUpdateWrapper<MessageRecord> wrapper = new LambdaUpdateWrapper<>();
        wrapper.eq(MessageRecord::getUserId, userId)
                .eq(MessageRecord::getReadStatus, 0)
                .set(MessageRecord::getReadStatus, 1)
                .set(MessageRecord::getReadTime, LocalDateTime.now());

        messageRecordMapper.update(null, wrapper);
    }


    /**
     * 分页查询消息记录（管理端）
     */
    @Override
    public IPage<MessageRecordVO> getMessagePage(Integer pageNum, Integer pageSize,
                                                 Long userId, String messageType, String channel,
                                                 Integer sendStatus, Integer readStatus) {
        log.info("分页查询消息记录 - 页码: {}, 每页大小: {}", pageNum, pageSize);

        Page<MessageRecordVO> page = new Page<>(pageNum, pageSize);
        IPage<MessageRecordVO> result = messageRecordMapper.selectMessagePage(
                page, userId, messageType, channel, sendStatus, readStatus);

        // 填充名称字段
        for (MessageRecordVO vo : result.getRecords()) {
            enrichMessageRecordVO(vo);
        }

        return result;
    }



    /**
     * 重发失败消息
     */
    @Override
    public void resendMessage(Long messageId) {
        log.info("重发消息 - 消息ID: {}", messageId);

        MessageRecord record = messageRecordMapper.selectById(messageId);
        if (record == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "消息不存在");
        }

        if (!SendStatus.FAILED.getCode().equals(record.getSendStatus())) {
            throw new BusinessException(ResultCode.PARAM_ERROR.getCode(), "只能重发失败的消息");
        }

        // 模拟重发（实际项目中对接短信/微信服务商）
        MessageRecord updateRecord = new MessageRecord();
        updateRecord.setId(messageId);
        updateRecord.setSendStatus(SendStatus.SENT.getCode());
        updateRecord.setSendTime(LocalDateTime.now());
        updateRecord.setErrorMsg(null);

        messageRecordMapper.updateById(updateRecord);
        log.info("消息重发成功 - 消息ID: {}", messageId);
    }

    /**
     * 删除消息记录
     */
    @Override
    public void deleteMessage(Long messageId) {
        log.info("删除消息 - 消息ID: {}", messageId);
        messageRecordMapper.deleteById(messageId);
    }

    /**
     * 批量删除消息记录
     */
    @Override
    public void batchDeleteMessage(List<Long> ids) {
        log.info("批量删除消息 - ids: {}", ids);
        messageRecordMapper.batchDelete(ids);
    }

    /**
     * 丰富MessageRecordVO信息（添加名称字段）
     */
    private void enrichMessageRecordVO(MessageRecordVO vo) {
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

        // 发送状态名称
        for (SendStatus status : SendStatus.values()) {
            if (status.getCode().equals(vo.getSendStatus())) {
                vo.setSendStatusName(status.getName());
                break;
            }
        }
    }

    /**
     * 发送叫号通知
     */
    @Override
    public void sendQueueCallNotice(Registration registration, String departmentName, String doctorName) {
        log.info("发送叫号通知 - 患者ID: {}, 排队号: {}", registration.getPatientId(), registration.getQueueNumber());

        User user = userMapper.selectById(registration.getPatientId());
        if (user == null) {
            log.warn("发送叫号通知失败：用户不存在 - 用户ID: {}", registration.getPatientId());
            return;
        }

        Map<String, String> params = new HashMap<>();
        params.put("patientName", user.getRealName());
        params.put("queueNumber", String.valueOf(registration.getQueueNumber()));
        params.put("departmentName", departmentName);

        sendMessageByTemplate(user.getId(), user.getPhone(), "QUEUE_CALL", params);
    }

    /**
     * 获取时间段名称
     */
    private String getTimeSlotName(String timeSlot) {
        if ("MORNING".equals(timeSlot)) return "上午";
        if ("AFTERNOON".equals(timeSlot)) return "下午";
        if ("EVENING".equals(timeSlot)) return "晚上";
        return timeSlot;
    }

    /**
     * 获取支付方式名称
     */
    private String getPaymentMethodName(String paymentMethod) {
        if ("WECHAT".equals(paymentMethod)) return "微信支付";
        if ("ALIPAY".equals(paymentMethod)) return "支付宝";
        if ("CASH".equals(paymentMethod)) return "现金";
        return paymentMethod;
    }

    /**
     * 解析模板，替换变量
     * @param template 模板内容
     * @param params 参数
     * @return 解析后的内容
     */
    private String parseTemplate(String template, Map<String, String> params) {
        if (template == null || params == null) {
            return template;
        }
        String result = template;
        for (Map.Entry<String, String> entry : params.entrySet()) {
            String placeholder = "{" + entry.getKey() + "}";
            String value = entry.getValue() != null ? entry.getValue() : "";
            result = result.replace(placeholder, value);
        }
        return result;
    }


    /**
     * 根据模板发送消息
     * @param userId 用户ID
     * @param phone 手机号
     * @param templateCode 模板编码
     * @param params 模板参数
     */
    private void sendMessageByTemplate(Long userId, String phone, String templateCode, Map<String, String> params) {
        MessageTemplate template = messageTemplateMapper.selectByTemplateCode(templateCode);
        if (template == null || template.getStatus() != 1) {
            log.warn("消息模板不存在或已禁用 - 模板编码: {}", templateCode);
            return;
        }

        String title = parseTemplate(template.getTitleTemplate(), params);
        String content = parseTemplate(template.getContentTemplate(), params);

        MessageRecord record = new MessageRecord();
        record.setTemplateId(template.getId());
        record.setUserId(userId);
        record.setMessageType(template.getMessageType());
        record.setChannel(template.getChannel());
        record.setTitle(title);
        record.setContent(content);
        record.setReceiver(phone != null ? phone : "");
        record.setSendStatus(SendStatus.SENT.getCode());
        record.setSendTime(LocalDateTime.now());
        record.setReadStatus(0);

        messageRecordMapper.insert(record);

        log.info("【消息发送】模板: {}, 接收人: {}", templateCode, phone);
    }

}


