package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.AssignPermissionDTO;
import com.hospital.registration.dto.RoleDTO;
import com.hospital.registration.service.RoleService;
import com.hospital.registration.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @title: RoleController
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/role")
public class RoleController {

    private final RoleService roleService;

    /**
     * 构造器注入
     */
    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    /**
     * 查询所有角色列表
     *
     * @return 角色列表
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询所有角色列表");
        List<RoleVO> roles = roleService.getAllRoles();
        return Result.ok().data("roles", roles);
    }

    /**
     * 分页查询角色列表
     *
     * @param current  当前页
     * @param size     每页大小
     * @param roleName 角色名称（可选）
     * @param roleCode 角色编码（可选）
     * @param status   状态（可选）
     * @return 分页结果
     */
    @GetMapping("/page")
    public Result page(
            @RequestParam(defaultValue = "1") Integer current,
            @RequestParam(defaultValue = "10") Integer size,
            @RequestParam(required = false) String roleName,
            @RequestParam(required = false) String roleCode,
            @RequestParam(required = false) Integer status) {
        log.info("分页查询角色列表 - current: {}, size: {}", current, size);
        Page<RoleVO> page = roleService.getRolePage(current, size, roleName, roleCode, status);
        return Result.ok().data("records", page.getRecords()).data("total", page.getTotal());
    }

    /**
     * 根据ID查询角色详情
     *
     * @param id 角色ID
     * @return 角色详情（包含权限ID列表）
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("查询角色详情 - ID: {}", id);
        RoleVO role = roleService.getRoleById(id);
        return Result.ok().data("role", role);
    }

    /**
     * 创建角色
     *
     * @param roleDTO 角色信息
     * @return 创建后的角色
     */
    @PostMapping("/add")
    @RequirePermission("system:role:add")
    @OperationLog(module = "角色管理", operation = "ADD")
    public Result add(@RequestBody RoleDTO roleDTO) {
        log.info("创建角色 - 角色编码: {}", roleDTO.getRoleCode());
        RoleVO role = roleService.createRole(roleDTO);
        return Result.ok("创建角色成功").data("role", role);
    }

    /**
     * 修改角色
     *
     * @param id      角色ID
     * @param roleDTO 角色信息
     * @return 修改后的角色
     */
    @PutMapping("/{id}")
    @RequirePermission("system:role:edit")
    @OperationLog(module = "角色管理", operation = "UPDATE")
    public Result update(@PathVariable Long id, @RequestBody RoleDTO roleDTO) {
        log.info("修改角色 - ID: {}", id);
        RoleVO role = roleService.updateRole(id, roleDTO);
        return Result.ok("修改角色成功").data("role", role);
    }

    /**
     * 删除角色
     *
     * @param id 角色ID
     * @return 操作结果
     */
    @DeleteMapping("/{id}")
    @RequirePermission("system:role:delete")
    @OperationLog(module = "角色管理", operation = "DELETE")
    public Result delete(@PathVariable Long id) {
        log.info("删除角色 - ID: {}", id);
        roleService.deleteRole(id);
        return Result.ok("删除角色成功");
    }

    /**
     * 为角色分配权限
     *
     * @param assignPermissionDTO 分配权限信息
     * @return 操作结果
     */
    @PostMapping("/assign-permissions")
    @RequirePermission("system:role:assign")
    @OperationLog(module = "角色管理", operation = "UPDATE")
    public Result assignPermissions(@RequestBody AssignPermissionDTO assignPermissionDTO) {
        log.info("为角色分配权限 - 角色ID: {}", assignPermissionDTO.getRoleId());
        roleService.assignPermissions(assignPermissionDTO);
        return Result.ok("分配权限成功");
    }

    /**
     * 查询角色的权限ID列表
     *
     * @param roleId 角色ID
     * @return 权限ID列表
     */
    @GetMapping("/{roleId}/permissions")
    public Result getRolePermissions(@PathVariable Long roleId) {
        log.info("查询角色权限ID列表 - 角色ID: {}", roleId);
        List<Long> permissionIds = roleService.getRolePermissionIds(roleId);
        return Result.ok().data("permissionIds", permissionIds);
    }

    /**
     * 批量更新角色状态
     *
     * @param params 包含 ids 和 status
     * @return 操作结果
     */
    @PutMapping("/batch-status")
    @RequirePermission("system:role:edit")
    @OperationLog(module = "角色管理", operation = "UPDATE")
    public Result batchUpdateStatus(@RequestBody Map<String, Object> params) {
        List<Integer> idList = (List<Integer>) params.get("ids");
        Integer status = (Integer) params.get("status");

        log.info("批量更新角色状态 - ids: {}, status: {}", idList, status);

        // 转换为 Long 类型
        List<Long> ids = idList.stream().map(Integer::longValue).toList();

        roleService.batchUpdateStatus(ids, status);
        return Result.ok("批量更新状态成功");
    }
}

