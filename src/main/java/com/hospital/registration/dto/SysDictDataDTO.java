package com.hospital.registration.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @title 数据字典数据DTO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典数据新增/修改请求参数
 */
@Data
public class SysDictDataDTO {

    // 字典数据ID（修改时必填）
    private Long id;

    // 字典类型编码
    @NotBlank(message = "字典类型编码不能为空")
    private String dictType;

    // 字典标签
    @NotBlank(message = "字典标签不能为空")
    private String dictLabel;

    // 字典值
    @NotBlank(message = "字典值不能为空")
    private String dictValue;

    // 排序
    private Integer sortOrder;

    // 状态
    private Integer status;

    // 备注
    private String remark;
}
