package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title: PaymentQueryDTO
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 支付查询DTO
 */
@Data
public class PaymentQueryDTO {

    private Integer pageNum = 1;

    private Integer pageSize = 10;

    // 交易流水号
    private String transactionNo;

    // 挂号单号
    private String registrationNo;

    // 支付状态
    private String paymentStatus;

    // 支付方式
    private String paymentMethod;

    // 开始日期
    private String startDate;

    // 结束日期
    private String endDate;
}
