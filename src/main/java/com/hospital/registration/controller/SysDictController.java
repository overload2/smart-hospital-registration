package com.hospital.registration.controller;

import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.SysDictDataDTO;
import com.hospital.registration.dto.SysDictTypeDTO;
import com.hospital.registration.service.SysDictService;
import com.hospital.registration.vo.SysDictDataVO;
import com.hospital.registration.vo.SysDictTypeVO;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title 数据字典Controller
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 数据字典接口
 */
@RestController
@RequestMapping("/sys/dict")
public class SysDictController {

    @Autowired
    private SysDictService sysDictService;

    /**
     * 获取所有字典类型(包含字典数据)
     */
    @GetMapping("/type/list")
    public Result listTypes() {
        List<SysDictTypeVO> list = sysDictService.getAllDictTypes();
        return Result.ok().data("list", list);
    }

    /**
     * 根据字典类型获取字典数据
     */
    @GetMapping("/data/list")
    public Result listDataByType(@RequestParam String dictType) {
        List<SysDictDataVO> list = sysDictService.getDictDataByType(dictType);
        return Result.ok().data("list", list);
    }

    /**
     * 新增字典类型
     */
    @PostMapping("/type/add")
    @OperationLog(module = "数据字典", operation = "ADD")
    public Result addType(@Valid @RequestBody SysDictTypeDTO dto) {
        sysDictService.addDictType(dto);
        return Result.ok().message("新增成功");
    }

    /**
     * 修改字典类型
     */
    @PostMapping("/type/update")
    @OperationLog(module = "数据字典", operation = "UPDATE")
    public Result updateType(@Valid @RequestBody SysDictTypeDTO dto) {
        sysDictService.updateDictType(dto);
        return Result.ok().message("修改成功");
    }

    /**
     * 删除字典类型
     */
    @PostMapping("/type/delete")
    @OperationLog(module = "数据字典", operation = "DELETE")
    public Result deleteType(@RequestParam Long id) {
        sysDictService.deleteDictType(id);
        return Result.ok().message("删除成功");
    }

    /**
     * 新增字典数据
     */
    @PostMapping("/data/add")
    @OperationLog(module = "数据字典", operation = "ADD")
    public Result addData(@Valid @RequestBody SysDictDataDTO dto) {
        sysDictService.addDictData(dto);
        return Result.ok().message("新增成功");
    }

    /**
     * 修改字典数据
     */
    @PostMapping("/data/update")
    @OperationLog(module = "数据字典", operation = "UPDATE")
    public Result updateData(@Valid @RequestBody SysDictDataDTO dto) {
        sysDictService.updateDictData(dto);
        return Result.ok().message("修改成功");
    }

    /**
     * 删除字典数据
     */
    @PostMapping("/data/delete")
    @OperationLog(module = "数据字典", operation = "DELETE")
    public Result deleteData(@RequestParam Long id) {
        sysDictService.deleteDictData(id);
        return Result.ok().message("删除成功");
    }
}
