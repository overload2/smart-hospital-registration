package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @title: DepartmentDTO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 科室DTO - 用于创建和更新科室
 */
@Data
public class DepartmentDTO {

    // 科室名称（必填，2-50位）
    @NotBlank(message = "科室名称不能为空")
    @Size(min = 2, max = 50, message = "科室名称长度必须在2-50位之间")
    private String name;

    // 科室编码（必填，唯一，2-20位）
    @NotBlank(message = "科室编码不能为空")
    @Pattern(regexp = "^[A-Z0-9_]{2,20}$", message = "科室编码只能包含大写字母、数字和下划线，长度2-20位")
    private String code;

    // 科室简介（可选，最多500字）
    @Size(max = 500, message = "科室简介不能超过500字")
    private String description;

    // 科室位置（可选，最多100字）
    @Size(max = 100, message = "科室位置不能超过100字")
    private String location;

    // 科室电话（可选，格式验证）
    @Pattern(regexp = "^(\\d{3,4}-)?\\d{7,8}$|^1[3-9]\\d{9}$",
            message = "电话格式不正确，支持固话(如0571-12345678)或手机号")
    private String phone;
}
