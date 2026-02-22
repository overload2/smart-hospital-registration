package com.hospital.registration.vo;

import lombok.Data;

/**
 * @title: DashboardStatsVO
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: 首页统计数据VO
 */
@Data
public class DashboardStatsVO {

    /** 今日预约数 */
    private Integer todayAppointments;

    /** 待就诊数 */
    private Integer pendingCount;

    /** 累计患者数（有就诊记录的） */
    private Integer totalPatients;

    /** 医生总数 */
    private Integer totalDoctors;
}
