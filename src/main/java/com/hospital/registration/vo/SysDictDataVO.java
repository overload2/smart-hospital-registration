package com.hospital.registration.vo;

import lombok.Data;

/**
 * @title 数据字典数据VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典数据返回信息
 */
@Data
public class SysDictDataVO {

    // 字典数据ID
    private Long id;

    // 字典类型编码
    private String dictType;

    // 字典标签
    private String dictLabel;

    // 字典值
    private String dictValue;

    // 排序
    private Integer sortOrder;

    // 状态
    private Integer status;
}
