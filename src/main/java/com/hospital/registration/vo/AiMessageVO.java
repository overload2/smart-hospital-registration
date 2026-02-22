package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title AI消息VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description AI消息返回信息
 */
@Data
public class AiMessageVO {

    // 消息ID
    private Long id;

    // 角色
    private String role;

    // 消息内容
    private String content;

    // 推荐科室ID
    private Long recommendedDepartmentId;

    // 推荐科室名称
    private String recommendedDepartmentName;

    // 推荐医生ID
    private Long recommendedDoctorId;

    // 推荐医生名称
    private String recommendedDoctorName;

    // 创建时间
    private LocalDateTime createTime;
}
