package com.hospital.registration.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.dto.AssignPermissionDTO;
import com.hospital.registration.dto.RoleDTO;
import com.hospital.registration.entity.Role;
import com.hospital.registration.entity.RolePermission;
import com.hospital.registration.mapper.RoleMapper;
import com.hospital.registration.mapper.RolePermissionMapper;
import com.hospital.registration.service.RoleService;
import com.hospital.registration.vo.RoleVO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @title: RoleServiceImpl
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 角色服务实现类
 */
@Slf4j
@Service
public class RoleServiceImpl implements RoleService {

    private final RoleMapper roleMapper;
    private final RolePermissionMapper rolePermissionMapper;

    /**
     * 构造器注入
     */
    public RoleServiceImpl(RoleMapper roleMapper, RolePermissionMapper rolePermissionMapper) {
        this.roleMapper = roleMapper;
        this.rolePermissionMapper = rolePermissionMapper;
    }

    /**
     * 查询所有角色列表
     */
    @Override
    public List<RoleVO> getAllRoles() {
        log.info("查询所有角色列表");

        List<Role> roles = roleMapper.selectAllRoles();
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : roles) {
            voList.add(convertToVO(role));
        }

        return voList;
    }

    /**
     * 根据ID查询角色详情
     */
    @Override
    public RoleVO getRoleById(Long id) {
        log.info("查询角色详情 - ID: {}", id);

        Role role = roleMapper.selectById(id);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "角色不存在");
        }

        RoleVO roleVO = convertToVO(role);
        // 查询角色的权限ID列表
        roleVO.setPermissionIds(rolePermissionMapper.selectPermissionIdsByRoleId(id));

        return roleVO;
    }

    /**
     * 创建角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO createRole(RoleDTO roleDTO) {
        log.info("创建角色 - 角色编码: {}", roleDTO.getRoleCode());

        // 检查角色编码是否已存在
        Role existRole = roleMapper.selectByRoleCode(roleDTO.getRoleCode());
        if (existRole != null) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "角色编码已存在");
        }

        // 创建角色
        Role role = new Role();
        BeanUtils.copyProperties(roleDTO, role);
        if (role.getStatus() == null) {
            role.setStatus(1);
        }

        int result = roleMapper.insert(role);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "创建角色失败");
        }

        log.info("创建角色成功 - ID: {}, 角色编码: {}", role.getId(), role.getRoleCode());
        return convertToVO(role);
    }

    /**
     * 修改角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public RoleVO updateRole(Long id, RoleDTO roleDTO) {
        log.info("修改角色 - ID: {}", id);

        // 检查角色是否存在
        Role role = roleMapper.selectById(id);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "角色不存在");
        }

        // 检查角色编码是否重复（排除自身）
        if (roleDTO.getRoleCode() != null && !roleDTO.getRoleCode().equals(role.getRoleCode())) {
            Role existRole = roleMapper.selectByRoleCode(roleDTO.getRoleCode());
            if (existRole != null) {
                throw new BusinessException(ResultCode.FAIL.getCode(), "角色编码已存在");
            }
        }

        // 更新角色
        Role updateRole = new Role();
        updateRole.setId(id);
        BeanUtils.copyProperties(roleDTO, updateRole);

        int result = roleMapper.updateById(updateRole);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "修改角色失败");
        }

        log.info("修改角色成功 - ID: {}", id);
        return getRoleById(id);
    }

    /**
     * 删除角色
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteRole(Long id) {
        log.info("删除角色 - ID: {}", id);

        // 检查角色是否存在
        Role role = roleMapper.selectById(id);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "角色不存在");
        }

        // 检查是否为系统内置角色（前4个角色不允许删除）
        if (id <= 4) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "系统内置角色不允许删除");
        }

        // 逻辑删除角色
        Role updateRole = new Role();
        updateRole.setId(id);
        updateRole.setDeleted(1);

        int result = roleMapper.updateById(updateRole);
        if (result <= 0) {
            throw new BusinessException(ResultCode.FAIL.getCode(), "删除角色失败");
        }

        // 删除角色权限关联
        rolePermissionMapper.deleteByRoleId(id);

        log.info("删除角色成功 - ID: {}", id);
    }

    /**
     * 为角色分配权限
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public void assignPermissions(AssignPermissionDTO assignPermissionDTO) {
        Long roleId = assignPermissionDTO.getRoleId();
        log.info("为角色分配权限 - 角色ID: {}, 权限数量: {}",
                roleId, assignPermissionDTO.getPermissionIds().size());

        // 检查角色是否存在
        Role role = roleMapper.selectById(roleId);
        if (role == null || role.getDeleted() == 1) {
            throw new BusinessException(ResultCode.NOT_FOUND.getCode(), "角色不存在");
        }

        // 先删除原有的角色权限关联
        rolePermissionMapper.deleteByRoleId(roleId);

        // 批量插入新的角色权限关联
        for (Long permissionId : assignPermissionDTO.getPermissionIds()) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionId);
            rolePermissionMapper.insert(rolePermission);
        }

        log.info("为角色分配权限成功 - 角色ID: {}", roleId);
    }

    /**
     * 查询角色的权限ID列表
     */
    @Override
    public List<Long> getRolePermissionIds(Long roleId) {
        log.info("查询角色权限ID列表 - 角色ID: {}", roleId);
        return rolePermissionMapper.selectPermissionIdsByRoleId(roleId);
    }

    /**
     * 分页查询角色列表
     */
    @Override
    public Page<RoleVO> getRolePage(Integer current, Integer size, String roleName, String roleCode, Integer status) {
        log.info("分页查询角色列表 - current: {}, size: {}, roleName: {}, roleCode: {}, status: {}",
                current, size, roleName, roleCode, status);

        // 创建分页对象
        Page<Role> page = new Page<>(current, size);

        // 调用Mapper查询
        Page<Role> rolePage = roleMapper.selectRolePage(page, roleName, roleCode, status);

        // 转换为VO
        Page<RoleVO> voPage = new Page<>(current, size, rolePage.getTotal());
        List<RoleVO> voList = new ArrayList<>();
        for (Role role : rolePage.getRecords()) {
            voList.add(convertToVO(role));
        }
        voPage.setRecords(voList);

        return voPage;
    }
    /**
     * 批量更新角色状态
     */
    @Override
    public void batchUpdateStatus(List<Long> ids, Integer status) {
        log.info("批量更新角色状态 - ids: {}, status: {}", ids, status);
        roleMapper.batchUpdateStatus(ids, status);
    }

    /**
     * Entity转VO
     */
    private RoleVO convertToVO(Role role) {
        RoleVO roleVO = new RoleVO();
        BeanUtils.copyProperties(role, roleVO);
        roleVO.setStatusName(role.getStatus() == 1 ? "启用" : "禁用");
        return roleVO;
    }
}

