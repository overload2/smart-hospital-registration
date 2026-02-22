package com.hospital.registration.vo;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @title 数据字典类型VO
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 字典类型返回信息
 */
@Data
public class SysDictTypeVO {

    // 字典类型ID
    private Long id;

    // 字典类型编码
    private String dictType;

    // 字典类型名称
    private String dictName;

    // 状态
    private Integer status;

    // 备注
    private String remark;

    // 更新时间
    private LocalDateTime updateTime;

    // 字典数据列表
    private List<SysDictDataVO> dataList;
}
