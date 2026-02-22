package com.hospital.registration.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.hospital.registration.entity.Payment;
import com.hospital.registration.entity.Registration;
import com.hospital.registration.vo.MessageRecordVO;

import java.util.List;

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

    /**
     * 分页查询消息记录（管理端）
     *
     * @param pageNum     页码
     * @param pageSize    每页大小
     * @param userId      用户ID
     * @param messageType 消息类型
     * @param channel     渠道
     * @param sendStatus  发送状态
     * @param readStatus  阅读状态
     * @return 分页结果
     */
    IPage<MessageRecordVO> getMessagePage(Integer pageNum, Integer pageSize,
                                          Long userId, String messageType, String channel,
                                          Integer sendStatus, Integer readStatus);



    /**
     * 重发失败消息
     *
     * @param messageId 消息ID
     */
    void resendMessage(Long messageId);

    /**
     * 删除消息记录
     *
     * @param messageId 消息ID
     */
    void deleteMessage(Long messageId);

    /**
     * 批量删除消息记录
     *
     * @param ids 消息ID列表
     */
    void batchDeleteMessage(List<Long> ids);

    /**
     * 发送叫号通知
     *
     * @param registration   挂号记录
     * @param departmentName 科室名称
     * @param doctorName     医生姓名
     */
    void sendQueueCallNotice(Registration registration, String departmentName, String doctorName);


}

