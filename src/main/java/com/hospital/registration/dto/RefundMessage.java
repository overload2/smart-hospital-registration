package com.hospital.registration.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * @title: RefundMessage
 * @author: Su
 * @date: 2026/2/14
 * @version: 1.0
 * @description: 退款消息实体
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class RefundMessage implements Serializable {

    // 挂号ID
    private Long registrationId;

    // 挂号单号
    private String registrationNo;

    // 退款金额
    private BigDecimal amount;

    // 患者ID
    private Long patientId;

    // 患者姓名
    private String patientName;
}

