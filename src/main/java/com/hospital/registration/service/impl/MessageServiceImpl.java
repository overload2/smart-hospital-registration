package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.MessageChannel;
import com.hospital.registration.common.MessageType;
import com.hospital.registration.common.SendStatus;
import com.hospital.registration.entity.MessageRecord;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.entity.User;
import com.hospital.registration.mapper.MessageRecordMapper;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.service.MessageService;
import com.hospital.registration.vo.MessageRecordVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;


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

    // 医院名称（用于消息前缀）
    private static final String HOSPITAL_NAME = "智慧医院";

    /**
     * 构造器注入
     */
    public MessageServiceImpl(MessageRecordMapper messageRecordMapper, UserMapper userMapper) {
        this.messageRecordMapper = messageRecordMapper;
        this.userMapper = userMapper;
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

        String timeSlotName = getTimeSlotName(registration.getTimeSlot().getTimeRange());
        String content = String.format(
                "【%s】您已成功预约挂号。挂号单号：%s，就诊日期：%s %s，科室：%s，医生：%s，排队号：%d，挂号费：%.2f元。请按时就诊！",
                HOSPITAL_NAME,
                registration.getRegistrationNo(),
                registration.getRegistrationDate(),
                timeSlotName,
                departmentName,
                doctorName,
                registration.getQueueNumber(),
                registration.getRegistrationFee()
        );

        saveAndSendMessage(user.getId(), user.getPhone(), MessageType.REGISTRATION_SUCCESS, "挂号成功通知", content);
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

        String paymentMethodName = getPaymentMethodName(payment.getPaymentMethod());
        String content = String.format(
                "【%s】您的挂号费已支付成功。支付金额：%.2f元，支付方式：%s，挂号单号：%s。请按时就诊！",
                HOSPITAL_NAME,
                payment.getAmount(),
                paymentMethodName,
                registration.getRegistrationNo()
        );

        saveAndSendMessage(user.getId(), user.getPhone(), MessageType.PAYMENT_SUCCESS, "支付成功通知", content);
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

        String content = String.format(
                "【%s】您的挂号已取消。挂号单号：%s，原就诊日期：%s，科室：%s，医生：%s。如已支付，退款将在1-3个工作日内到账。",
                HOSPITAL_NAME,
                registration.getRegistrationNo(),
                registration.getRegistrationDate(),
                departmentName,
                doctorName
        );

        saveAndSendMessage(user.getId(), user.getPhone(), MessageType.REGISTRATION_CANCEL, "挂号取消通知", content);
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

        String content = String.format(
                "【%s】您的挂号费已退款成功。退款金额：%.2f元，交易流水号：%s。退款已原路返回，请注意查收。",
                HOSPITAL_NAME,
                payment.getAmount(),
                payment.getTransactionNo()
        );

        saveAndSendMessage(user.getId(), user.getPhone(), MessageType.REFUND_SUCCESS, "退款成功通知", content);
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
     * 保存消息记录并模拟发送
     *
     * @param userId      用户ID
     * @param phone       手机号
     * @param messageType 消息类型
     * @param title       消息标题
     * @param content     消息内容
     */
    private void saveAndSendMessage(Long userId, String phone, MessageType messageType, String title, String content) {
        MessageRecord record = new MessageRecord();
        record.setUserId(userId);
        record.setMessageType(messageType.getCode());
        record.setChannel(MessageChannel.SMS.getCode());
        record.setTitle(title);
        record.setContent(content);
        record.setReceiver(phone);
        record.setSendStatus(SendStatus.SENT.getCode());
        record.setSendTime(LocalDateTime.now());
        record.setReadStatus(0);

        messageRecordMapper.insert(record);

        // 模拟发送短信（实际项目中对接短信服务商）
        log.info("【短信模拟】发送至：{}，内容：{}", phone, content);
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
}


