package com.hospital.registration.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * @title 数据字典类型实体类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 数据字典类型信息
 */
@Data
@TableName("sys_dict_type")
public class SysDictType {

    // 字典类型ID
    @TableId(type = IdType.AUTO)
    private Long id;

    // 字典类型编码
    private String dictType;

    // 字典类型名称
    private String dictName;

    // 状态(0禁用 1启用)
    private Integer status;

    // 备注
    private String remark;

    // 创建时间
    private LocalDateTime createTime;

    // 更新时间
    private LocalDateTime updateTime;
}
