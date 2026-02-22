package com.hospital.registration.dto;

import lombok.Data;

/**
 * @title UserQueryDTO
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 用户查询条件DTO
 */
@Data
public class UserQueryDTO {

    // 用户名
    private String username;

    // 真实姓名
    private String realName;

    // 手机号
    private String phone;

    // 状态
    private Integer status;

    // 页码
    private Integer pageNum = 1;

    // 每页数量
    private Integer pageSize = 10;
}