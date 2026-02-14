package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.*;
import com.hospital.registration.common.PaymentStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * @title: Payment
 * @author: Su
 * @date: 2026/1/11 19:50
 * @version: 1.0
 * @description: 支付记录表
 */
@Data
@TableName("payment")
public class Payment {

    // 主键（自增）
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    // 交易流水号（唯一）
    @TableField("transaction_no")
    private String transactionNo;

    // 挂号ID
    @TableField("registration_id")
    private Long registrationId;

    // 用户ID
    @TableField("user_id")
    private Long userId;

    // 支付金额
    private BigDecimal amount;

    // 支付方式：ALIPAY-支付宝，WECHAT-微信，CASH-现金
    @TableField("payment_method")
    private String paymentMethod;

    // 支付状态
    @TableField("payment_status")
    private PaymentStatus paymentStatus;

    // 支付时间
    @TableField("payment_time")
    private LocalDateTime paymentTime;

    // 退款时间
    @TableField("refund_time")
    private LocalDateTime refundTime;

    // 备注
    private String remark;

    // 创建时间（自动填充）
    @TableField(value = "create_time", fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    // 更新时间（自动填充）
    @TableField(value = "update_time", fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;

    // 逻辑删除标识：0-未删除，1-已删除
    @TableLogic
    @TableField(value = "deleted")
    private Integer deleted;
}
