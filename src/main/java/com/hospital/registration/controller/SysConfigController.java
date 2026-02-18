package com.hospital.registration.controller;

import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.SysConfigDTO;
import com.hospital.registration.service.SysConfigService;
import com.hospital.registration.vo.SysConfigVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title 系统配置Controller
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 系统配置接口
 */
@RestController
@RequestMapping("/sys/config")
public class SysConfigController {

    @Autowired
    private SysConfigService sysConfigService;

    /**
     * 获取所有配置
     */
    @GetMapping("/list")
    public Result list() {
        List<SysConfigVO> list = sysConfigService.getAllConfigs();
        return Result.ok().data("list", list);
    }

    /**
     * 根据配置键获取配置值
     */
    @GetMapping("/value")
    public Result getValue(@RequestParam String configKey) {
        String value = sysConfigService.getConfigValue(configKey);
        return Result.ok().data("value", value);
    }

    /**
     * 新增配置
     */
    @PostMapping("/add")
    @OperationLog(module = "系统配置", operation = "ADD")
    public Result add(@Valid @RequestBody SysConfigDTO dto) {
        sysConfigService.addConfig(dto);
        return Result.ok().message("新增成功");
    }

    /**
     * 修改配置
     */
    @PostMapping("/update")
    @OperationLog(module = "系统配置", operation = "UPDATE")
    public Result update(@Valid @RequestBody SysConfigDTO dto) {
        sysConfigService.updateConfig(dto);
        return Result.ok().message("修改成功");
    }

    /**
     * 删除配置
     */
    @PostMapping("/delete")
    @OperationLog(module = "系统配置", operation = "DELETE")
    public Result delete(@RequestParam Long id) {
        sysConfigService.deleteConfig(id);
        return Result.ok().message("删除成功");
    }
}
