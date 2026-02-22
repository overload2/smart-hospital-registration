package com.hospital.registration.controller;

import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.AssignRoleDTO;
import com.hospital.registration.dto.PermissionDTO;
import com.hospital.registration.service.PermissionService;
import com.hospital.registration.vo.PermissionVO;
import com.hospital.registration.vo.UserRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title: PermissionController
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/permission")
public class PermissionController {

    private final PermissionService permissionService;

    /**
     * 构造器注入
     */
    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    /**
     * 查询权限树
     *
     * @return 权限树形列表
     */
    @GetMapping("/tree")
    public Result tree() {
        log.info("查询权限树");
        List<PermissionVO> tree = permissionService.getPermissionTree();
        return Result.ok().data("permissions", tree);
    }

    /**
     * 查询所有权限列表（平铺）
     *
     * @return 权限列表
     */
    @GetMapping("/list")
    public Result list() {
        log.info("查询所有权限列表");
        List<PermissionVO> permissions = permissionService.getAllPermissions();
        return Result.ok().data("permissions", permissions);
    }

    /**
     * 根据ID查询权限详情
     *
     * @param id 权限ID
     * @return 权限详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("查询权限详情 - ID: {}", id);
        PermissionVO permission = permissionService.getPermissionById(id);
        return Result.ok().data("permission", permission);
    }

    /**
     * 创建权限
     *
     * @param permissionDTO 权限信息
     * @return 创建后的权限
     */
    @PostMapping("/add")
    @OperationLog(module = "权限管理", operation = "ADD")
    public Result add(@RequestBody PermissionDTO permissionDTO) {
        log.info("创建权限 - 权限编码: {}", permissionDTO.getPermissionCode());
        PermissionVO permission = permissionService.createPermission(permissionDTO);
        return Result.ok("创建权限成功").data("permission", permission);
    }

    /**
     * 修改权限
     *
     * @param id            权限ID
     * @param permissionDTO 权限信息
     * @return 修改后的权限
     */
    @PostMapping("/{id}")
    @OperationLog(module = "权限管理", operation = "UPDATE")
    public Result update(@PathVariable Long id, @RequestBody PermissionDTO permissionDTO) {
        log.info("修改权限 - ID: {}", id);
        PermissionVO permission = permissionService.updatePermission(id, permissionDTO);
        return Result.ok("修改权限成功").data("permission", permission);
    }

    /**
     * 删除权限
     */
    @PostMapping("/delete/{id}")
    @OperationLog(module = "权限管理", operation = "DELETE")
    public Result delete(@PathVariable Long id) {
        log.info("删除权限 - ID: {}", id);
        permissionService.deletePermission(id);
        return Result.ok("删除权限成功");
    }
    /**
     * 更新权限状态（级联）
     * 禁用时会同时禁用所有子权限
     * 启用时会同时启用所有父权限
     */
    @PostMapping("/{id}/status")
    @OperationLog(module = "权限管理", operation = "UPDATE")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新权限状态（级联） - ID: {}, status: {}", id, status);
        permissionService.updatePermissionStatusCascade(id, status);
        String message = status == 1 ? "启用成功" : "禁用成功";
        return Result.ok(message);
    }

    /**
     * 批量更新权限状态（级联）
     */
    @PostMapping("/batch-status")
    @OperationLog(module = "权限管理", operation = "UPDATE")
    public Result batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam Integer status) {
        log.info("批量更新权限状态 - ids: {}, status: {}", ids, status);
        permissionService.batchUpdatePermissionStatus(ids, status);
        String message = status == 1 ? "批量启用成功" : "批量禁用成功";
        return Result.ok(message);
    }

    /**
     * 为用户分配角色
     *
     * @param assignRoleDTO 分配角色信息
     * @return 操作结果
     */
    @PostMapping("/assign-roles")
    @OperationLog(module = "权限管理", operation = "UPDATE")
    public Result assignRoles(@RequestBody AssignRoleDTO assignRoleDTO) {
        log.info("为用户分配角色 - 用户ID: {}", assignRoleDTO.getUserId());
        permissionService.assignRolesToUser(assignRoleDTO);
        return Result.ok("分配角色成功");
    }

    /**
     * 查询用户的角色和权限
     *
     * @param userId 用户ID
     * @return 用户角色和权限信息
     */
    @GetMapping("/user/{userId}/roles")
    public Result getUserRoles(@PathVariable Long userId) {
        log.info("查询用户角色和权限 - 用户ID: {}", userId);
        UserRoleVO userRoleVO = permissionService.getUserRoles(userId);
        return Result.ok().data("userRole", userRoleVO);
    }

    /**
     * 查询用户的权限编码列表
     *
     * @param userId 用户ID
     * @return 权限编码列表
     */
    @GetMapping("/user/{userId}/permissions")
    public Result getUserPermissions(@PathVariable Long userId) {
        log.info("查询用户权限编码列表 - 用户ID: {}", userId);
        List<String> permissions = permissionService.getUserPermissionCodes(userId);
        return Result.ok().data("permissions", permissions);
    }
}

