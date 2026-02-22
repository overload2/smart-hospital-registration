package com.hospital.registration.dto;

import com.hospital.registration.common.TimeSlot;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

/**
 * @title: ScheduleDTO
 * @author: Su
 * @date: 2026/1/18
 * @version: 1.0
 * @description: 排班DTO - 用于创建和更新排班信息
 */
@Data
public class ScheduleDTO {

    // 医生ID（必填）
    @NotNull(message = "医生ID不能为空")
    private Long doctorId;

    // 科室ID（必填）
    @NotNull(message = "科室ID不能为空")
    private Long departmentId;

    // 排班日期（必填）
    @NotNull(message = "排班日期不能为空")
    @Future(message = "排班日期必须是未来日期")
    private LocalDate scheduleDate;

    // 时间段（必填）
    @NotNull(message = "时间段不能为空")
    private TimeSlot timeSlot;

    // 总号源数（必填，1-100之间）
    @NotNull(message = "总号源数不能为空")
    @Min(value = 1, message = "总号源数至少为1")
    @Max(value = 100, message = "总号源数不能超过100")
    private Integer totalNumber;
}

