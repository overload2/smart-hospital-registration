package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title: DepartmentVO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 科室VO - 返回给前端的科室信息
 */
@Data
public class DepartmentVO {

    // 科室ID
    private Long id;

    // 科室名称
    private String name;

    // 科室编码
    private String code;

    // 科室简介
    private String description;

    // 科室位置
    private String location;

    // 科室电话
    private String phone;

    // 状态：0-禁用，1-启用
    private Integer status;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
