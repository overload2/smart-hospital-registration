package com.hospital.registration.service.impl;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.AssignRoleDTO;
import com.hospital.registration.dto.PermissionDTO;
import com.hospital.registration.entity.Permission;
import com.hospital.registration.entity.Role;
import com.hospital.registration.entity.User;
import com.hospital.registration.entity.UserRole;
import com.hospital.registration.mapper.PermissionMapper;
import com.hospital.registration.mapper.UserMapper;
import com.hospital.registration.mapper.UserRoleMapper;
import com.hospital.registration.service.PermissionService;
import com.hospital.registration.vo.PermissionVO;
import com.hospital.registration.vo.RoleVO;
import com.hospital.registration.vo.UserRoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: PermissionServiceImpl
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限服务实现类
 */
@Slf4j
@Service
public class PermissionServiceImpl implements PermissionService {

    private final PermissionMapper permissionMapper;
    private final UserRoleMapper userRoleMapper;
    private final UserMapper userMapper;

    /**
     * 构造器注入
     */
    public PermissionServiceImpl(PermissionMapper permissionMapper,
                                 UserRoleMapper userRoleMapper,
                                 UserMapper userMapper) {
        this.permissionMapper = permissionMapper;
        this.userRoleMapper = userRoleMapper;
        this.userMapper = userMapper;
    }

    /**
     * 查询权限树
     */
    @Override
    public List<PermissionVO> getPermissionTree() {
        log.info("查询权限树");

        // 查询所有权限
        List<PermissionVO> allPermissions = getAllPermissions();

        // 构建树形结构
        return buildTree(allPermissions, 0L);
    }

    /**
     * 查询所有权限列表（平铺）
     */
    @Override
    public List<PermissionVO> getAllPermissions() {
        log.info("查询所有权限列表");

        List<Permission> permissions = permissionMapper.selectAllPermissions();
        List<PermissionVO> voList = new ArrayList<>();
        for (Permission permission : permissions) {
            voList.add(convertToVO(permission));
        }

        return voList;
    }

    /**
     * 根据ID查询权限详情
     */
    @Override
    public PermissionVO getPermissionById(Long id) {
        log.info("查询权限详情 - ID: {}", id);

        Permission permission = permissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "权限不存在");
        }

        return convertToVO(permission);
    }

    /**
     * 创建权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionVO createPermission(PermissionDTO permissionDTO) {
        log.info("创建权限 - 权限编码: {}", permissionDTO.getPermissionCode());

        // 检查权限编码是否已存在
        Permission existPermission = permissionMapper.selectByPermissionCode(permissionDTO.getPermissionCode());
        if (existPermission != null) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "权限编码已存在");
        }

        // 创建权限
        Permission permission = new Permission();
        BeanUtils.copyProperties(permissionDTO, permission);
        if (permission.getParentId() == null) {
            permission.setParentId(0L);
        }
        if (permission.getSortOrder() == null) {
            permission.setSortOrder(0);
        }
        if (permission.getStatus() == null) {
            permission.setStatus(1);
        }

        int result = permissionMapper.insert(permission);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "创建权限失败");
        }

        log.info("创建权限成功 - ID: {}, 权限编码: {}", permission.getId(), permission.getPermissionCode());
        return convertToVO(permission);
    }

    /**
     * 修改权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public PermissionVO updatePermission(Long id, PermissionDTO permissionDTO) {
        log.info("修改权限 - ID: {}", id);

        // 检查权限是否存在
        Permission permission = permissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "权限不存在");
        }

        // 检查权限编码是否重复（排除自身）
        if (permissionDTO.getPermissionCode() != null &&
                !permissionDTO.getPermissionCode().equals(permission.getPermissionCode())) {
            Permission existPermission = permissionMapper.selectByPermissionCode(permissionDTO.getPermissionCode());
            if (existPermission != null) {
                throw new BusinessException(ResultCode.FAIL.getCode(), "权限编码已存在");
            }
        }

        // 更新权限
        Permission updatePermission = new Permission();
        updatePermission.setId(id);
        BeanUtils.copyProperties(permissionDTO, updatePermission);

        int result = permissionMapper.updateById(updatePermission);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "修改权限失败");
        }

        log.info("修改权限成功 - ID: {}", id);
        return getPermissionById(id);
    }

    /**
     * 删除权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deletePermission(Long id) {
        log.info("删除权限 - ID: {}", id);

        // 检查权限是否存在
        Permission permission = permissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "权限不存在");
        }

        // 检查是否有子权限
        Integer childCount = permissionMapper.countByParentId(id);
        if (childCount > 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "存在子权限，无法删除");
        }

        // 逻辑删除权限
        Permission updatePermission = new Permission();
        updatePermission.setId(id);
        updatePermission.setDeleted(1);

        int result = permissionMapper.updateById(updatePermission);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "删除权限失败");
        }

        log.info("删除权限成功 - ID: {}", id);
    }

    /**
     * 级联更新权限状态
     * 禁用时：同时禁用所有子权限
     * 启用时：同时启用所有父权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePermissionStatusCascade(Long id, Integer status) {
        log.info("级联更新权限状态 - ID: {}, status: {}", id, status);

        Permission permission = permissionMapper.selectById(id);
        if (permission == null || permission.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "权限不存在");
        }

        List<Long> idsToUpdate = new ArrayList<>();
        idsToUpdate.add(id);

        if (status == 0) {
            // 禁用时：递归收集所有子权限ID
            collectAllChildrenIds(id, idsToUpdate);
            log.info("禁用权限及其子权限 - 共 {} 个", idsToUpdate.size());
        } else {
            // 启用时：递归收集所有父权限ID
            collectAllParentIds(id, idsToUpdate);
            log.info("启用权限及其父权限 - 共 {} 个", idsToUpdate.size());
        }

        // 批量更新状态
        permissionMapper.batchUpdateStatus(idsToUpdate, status);
    }

    /**
     * 递归收集所有子权限ID
     */
    private void collectAllChildrenIds(Long parentId, List<Long> result) {
        List<Long> childrenIds = permissionMapper.selectDirectChildrenIds(parentId);
        if (childrenIds != null && !childrenIds.isEmpty()) {
            for (Long childId : childrenIds) {
                if (!result.contains(childId)) {
                    result.add(childId);
                    // 递归查询子权限的子权限
                    collectAllChildrenIds(childId, result);
                }
            }
        }
    }

    /**
     * 递归收集所有父权限ID
     */
    private void collectAllParentIds(Long id, List<Long> result) {
        Long parentId = permissionMapper.selectParentIdById(id);
        if (parentId != null && parentId != 0 && !result.contains(parentId)) {
            result.add(parentId);
            // 递归查询父权限的父权限
            collectAllParentIds(parentId, result);
        }
    }

    /**
     * 批量更新权限状态（带级联）
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void batchUpdatePermissionStatus(List<Long> ids, Integer status) {
        log.info("批量更新权限状态 - ids: {}, status: {}", ids, status);

        if (ids == null || ids.isEmpty()) {
            return;
        }

        // 收集所有需要更新的ID（包括级联的）
        List<Long> allIdsToUpdate = new ArrayList<>();

        for (Long id : ids) {
            if (!allIdsToUpdate.contains(id)) {
                allIdsToUpdate.add(id);
            }

            if (status == 0) {
                // 禁用时：收集所有子权限
                collectAllChildrenIds(id, allIdsToUpdate);
            } else {
                // 启用时：收集所有父权限
                collectAllParentIds(id, allIdsToUpdate);
            }
        }

        log.info("批量更新权限状态 - 实际更新 {} 个权限", allIdsToUpdate.size());
        permissionMapper.batchUpdateStatus(allIdsToUpdate, status);
    }

    /**
     * 更新权限状态（简单更新，不级联）
     * 保留原方法供特殊场景使用
     */
    @Override
    public void updatePermissionStatus(Long id, Integer status) {
        log.info("更新权限状态 - ID: {}, status: {}", id, status);

        Permission permission = permissionMapper.selectById(id);
        if (permission == null) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "权限不存在");
        }

        Permission updatePermission = new Permission();
        updatePermission.setId(id);
        updatePermission.setStatus(status);
        permissionMapper.updateById(updatePermission);
    }

    /**
     * 为用户分配角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignRolesToUser(AssignRoleDTO assignRoleDTO) {
        Long userId = assignRoleDTO.getUserId();
        log.info("为用户分配角色 - 用户ID: {}, 角色数量: {}",
                userId, assignRoleDTO.getRoleIds().size());

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        // 先删除原有的用户角色关联
        userRoleMapper.deleteByUserId(userId);

        // 批量插入新的用户角色关联
        for (Long roleId : assignRoleDTO.getRoleIds()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userId);
            userRole.setRoleId(roleId);
            userRoleMapper.insert(userRole);
        }

        log.info("为用户分配角色成功 - 用户ID: {}", userId);
    }

    /**
     * 查询用户的角色和权限
     */
    @Override
    public UserRoleVO getUserRoles(Long userId) {
        log.info("查询用户角色和权限 - 用户ID: {}", userId);

        // 检查用户是否存在
        User user = userMapper.selectById(userId);
        if (user == null || user.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "用户不存在");
        }

        UserRoleVO userRoleVO = new UserRoleVO();
        userRoleVO.setUserId(userId);
        userRoleVO.setUsername(user.getUsername());
        userRoleVO.setRealName(user.getRealName());

        // 查询用户的角色列表
        List<Role> roles = userRoleMapper.selectRolesByUserId(userId);
        List<RoleVO> roleVOs = new ArrayList<>();
        for (Role role : roles) {
            RoleVO roleVO = new RoleVO();
            BeanUtils.copyProperties(role, roleVO);
            roleVO.setStatusName(role.getStatus() == 1 ? "启用" : "禁用");
            roleVOs.add(roleVO);
        }
        userRoleVO.setRoles(roleVOs);

        // 查询用户的权限编码列表
        userRoleVO.setPermissions(getUserPermissionCodes(userId));

        return userRoleVO;
    }

    /**
     * 查询用户的权限编码列表
     */
    @Override
    public List<String> getUserPermissionCodes(Long userId) {
        log.info("查询用户权限编码列表 - 用户ID: {}", userId);

        List<Permission> permissions = permissionMapper.selectByUserId(userId);
        List<String> codes = new ArrayList<>();
        for (Permission permission : permissions) {
            codes.add(permission.getPermissionCode());
        }

        return codes;
    }

    /**
     * 检查用户是否拥有指定权限
     */
    @Override
    public boolean hasPermission(Long userId, String permissionCode) {
        log.info("检查用户权限 - 用户ID: {}, 权限编码: {}", userId, permissionCode);

        List<String> permissionCodes = getUserPermissionCodes(userId);
        return permissionCodes.contains(permissionCode);
    }

    /**
     * 判断用户是否是超级管理员
     */
    @Override
    public boolean isSuperAdmin(Long userId) {
        // 查询用户的角色编码，判断是否包含 ADMIN
        List<String> roleCodes = userRoleMapper.selectRoleCodesByUserId(userId);
        return roleCodes.contains("ADMIN");
    }

    /**
     * 构建权限树
     *
     * @param allPermissions 所有权限列表
     * @param parentId       父权限ID
     * @return 树形权限列表
     */
    private List<PermissionVO> buildTree(List<PermissionVO> allPermissions, Long parentId) {
        List<PermissionVO> tree = new ArrayList<>();

        for (PermissionVO permission : allPermissions) {
            if (permission.getParentId().equals(parentId)) {
                // 递归查找子权限
                permission.setChildren(buildTree(allPermissions, permission.getId()));
                tree.add(permission);
            }
        }

        return tree;
    }

    /**
     * Entity转VO
     */
    private PermissionVO convertToVO(Permission permission) {
        PermissionVO permissionVO = new PermissionVO();
        BeanUtils.copyProperties(permission, permissionVO);
        permissionVO.setStatusName(permission.getStatus() == 1 ? "启用" : "禁用");
        permissionVO.setPermissionTypeName("MENU".equals(permission.getPermissionType()) ? "菜单" : "按钮");
        return permissionVO;
    }
}
