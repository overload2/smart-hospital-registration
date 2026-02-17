package com.hospital.registration.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.vo.MessageRecordVO;

/**
 * @title: MessageService
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 消息服务接口
 */
public interface MessageService {

    /**
     * 发送挂号成功通知
     *
     * @param registration   挂号记录
     * @param departmentName 科室名称
     * @param doctorName     医生姓名
     */
    void sendRegistrationSuccessNotice(Registration registration, String departmentName, String doctorName);

    /**
     * 发送支付成功通知
     *
     * @param payment      支付记录
     * @param registration 挂号记录
     */
    void sendPaymentSuccessNotice(Payment payment, Registration registration);

    /**
     * 发送挂号取消通知
     *
     * @param registration   挂号记录
     * @param departmentName 科室名称
     * @param doctorName     医生姓名
     */
    void sendRegistrationCancelNotice(Registration registration, String departmentName, String doctorName);

    /**
     * 发送退款成功通知
     *
     * @param payment 支付记录
     */
    void sendRefundSuccessNotice(Payment payment);

    /**
     * 分页查询用户消息列表
     *
     * @param userId   用户ID
     * @param pageNum  页码
     * @param pageSize 每页大小
     * @return 消息VO分页对象
     */
    IPage<MessageRecordVO> getUserMessages(Long userId, Integer pageNum, Integer pageSize);

    /**
     * 获取用户未读消息数量
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Integer getUnreadCount(Long userId);

    /**
     * 标记消息已读
     *
     * @param messageId 消息ID
     * @param userId    用户ID
     */
    void markAsRead(Long messageId, Long userId);

    /**
     * 全部标记已读
     *
     * @param userId 用户ID
     */
    void markAllAsRead(Long userId);
}

