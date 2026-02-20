package com.hospital.registration.controller;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.common.Result;
import com.hospital.registration.dto.UserDTO;
import com.hospital.registration.dto.UserQueryDTO;
import com.hospital.registration.service.UserService;
import com.hospital.registration.vo.UserVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @title SysUserController
 * @author Su
 * @date 2026/2/20
 * @version 1.0
 * @description 系统管理-用户管理控制器
 */
@Slf4j
@RestController
@RequestMapping("/sys/user")
public class SysUserController {

    private final UserService userService;

    public SysUserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * 分页查询用户列表
     */
    @PostMapping("/page")
    public Result page(@RequestBody UserQueryDTO queryDTO) {
        log.info("分页查询用户列表");
        Page<UserVO> page = userService.getUserPage(queryDTO);
        return Result.ok().data("page", page);
    }

    /**
     * 获取用户详情
     */
    @GetMapping("/{id}")
    public Result getById(@PathVariable Long id) {
        log.info("获取用户详情 - ID: {}", id);
        UserVO user = userService.getUserById(id);
        return Result.ok().data("user", user);
    }

    /**
     * 新增用户
     */
    @PostMapping("/add")
    @OperationLog(module = "用户管理", operation = "ADD")
    public Result add(@RequestBody UserDTO userDTO) {
        log.info("新增用户 - 用户名: {}", userDTO.getUsername());
        UserVO user = userService.createUser(userDTO);
        return Result.ok("新增用户成功").data("user", user);
    }

    /**
     * 修改用户
     */
    @PostMapping("/{id}")
    @OperationLog(module = "用户管理", operation = "UPDATE")
    public Result update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
        log.info("修改用户 - ID: {}", id);
        UserVO user = userService.updateUser(id, userDTO);
        return Result.ok("修改用户成功").data("user", user);
    }

    /**
     * 删除用户
     */
    @PostMapping("/delete/{id}")
    @OperationLog(module = "用户管理", operation = "DELETE")
    public Result delete(@PathVariable Long id) {
        log.info("删除用户 - ID: {}", id);
        userService.deleteUser(id);
        return Result.ok("删除用户成功");
    }

    /**
     * 批量删除用户
     */
    @PostMapping("/batch-delete")
    @OperationLog(module = "用户管理", operation = "DELETE")
    public Result batchDelete(@RequestBody List<Long> ids) {
        log.info("批量删除用户 - ids: {}", ids);
        userService.batchDeleteUser(ids);
        return Result.ok("批量删除成功");
    }

    /**
     * 更新用户状态
     */
    @PostMapping("/{id}/status")
    @OperationLog(module = "用户管理", operation = "UPDATE")
    public Result updateStatus(@PathVariable Long id, @RequestParam Integer status) {
        log.info("更新用户状态 - ID: {}, status: {}", id, status);
        userService.updateUserStatus(id, status);
        String message = status == 1 ? "启用成功" : "禁用成功";
        return Result.ok(message);
    }

    /**
     * 批量更新用户状态
     */
    @PostMapping("/batch-status")
    @OperationLog(module = "用户管理", operation = "UPDATE")
    public Result batchUpdateStatus(@RequestBody List<Long> ids, @RequestParam Integer status) {
        log.info("批量更新用户状态 - ids: {}, status: {}", ids, status);
        userService.batchUpdateUserStatus(ids, status);
        String message = status == 1 ? "批量启用成功" : "批量禁用成功";
        return Result.ok(message);
    }

    /**
     * 重置用户密码
     */
    @PostMapping("/{id}/reset-password")
    @OperationLog(module = "用户管理", operation = "UPDATE")
    public Result resetPassword(@PathVariable Long id) {
        log.info("重置用户密码 - ID: {}", id);
        String defaultPassword = "123456";
        userService.resetPassword(id, defaultPassword);
        return Result.ok("密码重置成功");
    }

    /**
     * 为用户分配角色
     */
    @PostMapping("/{id}/assign-roles")
    @OperationLog(module = "用户管理", operation = "UPDATE")
    public Result assignRoles(@PathVariable Long id, @RequestBody List<Long> roleIds) {
        log.info("为用户分配角色 - userId: {}, roleIds: {}", id, roleIds);
        userService.assignRoles(id, roleIds);
        return Result.ok("分配角色成功");
    }
}
