package com.hospital.registration.vo;

import lombok.Data;

/**
 * @title AI对话响应VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI对话响应信息
 */
@Data
public class AiChatVO {

    // 会话ID
    private Long sessionId;

    // AI回复内容
    private String reply;

    // 推荐科室ID（导诊时）
    private Long recommendedDepartmentId;

    // 推荐科室名称
    private String recommendedDepartmentName;

    // 推荐医生ID（导诊时）
    private Long recommendedDoctorId;

    // 推荐医生名称
    private String recommendedDoctorName;
}
