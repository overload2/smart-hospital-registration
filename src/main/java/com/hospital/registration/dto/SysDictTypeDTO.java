package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title 数据字典类型DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典类型新增/修改请求参数
 */
@Data
public class SysDictTypeDTO {

    // 字典类型ID（修改时必填）
    private Long id;

    // 字典类型编码
    @NotBlank(message = "字典类型编码不能为空")
    private String dictType;

    // 字典类型名称
    @NotBlank(message = "字典类型名称不能为空")
    private String dictName;

    // 状态
    private Integer status;

    // 备注
    private String remark;
}
