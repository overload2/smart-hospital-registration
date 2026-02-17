-- =============================================
-- RBAC权限管理相关表
-- =============================================

-- 1. 角色表
CREATE TABLE `role` (
                        `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                        `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
                        `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
                        `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
                        `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                        `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                        PRIMARY KEY (`id`),
                        UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 2. 权限表
CREATE TABLE `permission` (
                              `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                              `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID（0表示顶级）',
                              `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
                              `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
                              `permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型：MENU-菜单，BUTTON-按钮',
                              `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
                              `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
                              `sort_order` INT DEFAULT 0 COMMENT '排序号',
                              `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                              `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                              PRIMARY KEY (`id`),
                              UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 3. 用户角色关联表
CREATE TABLE `user_role` (
                             `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                             `user_id` BIGINT NOT NULL COMMENT '用户ID',
                             `role_id` BIGINT NOT NULL COMMENT '角色ID',
                             `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                             PRIMARY KEY (`id`),
                             UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 4. 角色权限关联表
CREATE TABLE `role_permission` (
                                   `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                   `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                   `permission_id` BIGINT NOT NULL COMMENT '权限ID',
                                   `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                   PRIMARY KEY (`id`),
                                   UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- =============================================
-- 初始化数据
-- =============================================

-- 初始化角色
INSERT INTO `role` (`role_code`, `role_name`, `description`) VALUES
                                                                 ('SUPER_ADMIN', '超级管理员', '拥有系统所有权限'),
                                                                 ('ADMIN', '管理员', '管理科室、医生、排班等基础数据'),
                                                                 ('DOCTOR', '医生', '查看接诊列表、填写病历'),
                                                                 ('PATIENT', '患者', '挂号预约、查询病历、评价医生');

-- 初始化权限（树形结构）
INSERT INTO `permission` (`parent_id`, `permission_code`, `permission_name`, `permission_type`, `sort_order`) VALUES
                                                                                                                  -- 系统管理
                                                                                                                  (0, 'system', '系统管理', 'MENU', 1),
                                                                                                                  (1, 'system:user', '用户管理', 'MENU', 1),
                                                                                                                  (2, 'system:user:list', '用户列表', 'BUTTON', 1),
                                                                                                                  (2, 'system:user:add', '新增用户', 'BUTTON', 2),
                                                                                                                  (2, 'system:user:edit', '编辑用户', 'BUTTON', 3),
                                                                                                                  (2, 'system:user:delete', '删除用户', 'BUTTON', 4),
                                                                                                                  (1, 'system:role', '角色管理', 'MENU', 2),
                                                                                                                  (7, 'system:role:list', '角色列表', 'BUTTON', 1),
                                                                                                                  (7, 'system:role:add', '新增角色', 'BUTTON', 2),
                                                                                                                  (7, 'system:role:edit', '编辑角色', 'BUTTON', 3),
                                                                                                                  (7, 'system:role:delete', '删除角色', 'BUTTON', 4),
                                                                                                                  (7, 'system:role:assign', '分配权限', 'BUTTON', 5),

                                                                                                                  -- 科室管理
                                                                                                                  (0, 'department', '科室管理', 'MENU', 2),
                                                                                                                  (13, 'department:list', '科室列表', 'BUTTON', 1),
                                                                                                                  (13, 'department:add', '新增科室', 'BUTTON', 2),
                                                                                                                  (13, 'department:edit', '编辑科室', 'BUTTON', 3),
                                                                                                                  (13, 'department:delete', '删除科室', 'BUTTON', 4),

                                                                                                                  -- 医生管理
                                                                                                                  (0, 'doctor', '医生管理', 'MENU', 3),
                                                                                                                  (18, 'doctor:list', '医生列表', 'BUTTON', 1),
                                                                                                                  (18, 'doctor:add', '新增医生', 'BUTTON', 2),
                                                                                                                  (18, 'doctor:edit', '编辑医生', 'BUTTON', 3),
                                                                                                                  (18, 'doctor:delete', '删除医生', 'BUTTON', 4),

                                                                                                                  -- 排班管理
                                                                                                                  (0, 'schedule', '排班管理', 'MENU', 4),
                                                                                                                  (23, 'schedule:list', '排班列表', 'BUTTON', 1),
                                                                                                                  (23, 'schedule:add', '新增排班', 'BUTTON', 2),
                                                                                                                  (23, 'schedule:edit', '编辑排班', 'BUTTON', 3),
                                                                                                                  (23, 'schedule:delete', '删除排班', 'BUTTON', 4),

                                                                                                                  -- 挂号管理
                                                                                                                  (0, 'registration', '挂号管理', 'MENU', 5),
                                                                                                                  (28, 'registration:list', '挂号列表', 'BUTTON', 1),
                                                                                                                  (28, 'registration:create', '创建挂号', 'BUTTON', 2),
                                                                                                                  (28, 'registration:cancel', '取消挂号', 'BUTTON', 3),
                                                                                                                  (28, 'registration:detail', '挂号详情', 'BUTTON', 4),

                                                                                                                  -- 病历管理
                                                                                                                  (0, 'medical', '病历管理', 'MENU', 6),
                                                                                                                  (33, 'medical:list', '病历列表', 'BUTTON', 1),
                                                                                                                  (33, 'medical:add', '新增病历', 'BUTTON', 2),
                                                                                                                  (33, 'medical:edit', '编辑病历', 'BUTTON', 3),
                                                                                                                  (33, 'medical:detail', '病历详情', 'BUTTON', 4);

-- 为超级管理员分配所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission`;

-- 为管理员分配部分权限（科室、医生、排班管理）
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 2, id FROM `permission` WHERE permission_code LIKE 'department%' OR permission_code LIKE 'doctor%' OR permission_code LIKE 'schedule%';

-- 为医生分配权限（挂号查看、病历管理）
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 3, id FROM `permission` WHERE permission_code IN ('registration', 'registration:list', 'registration:detail', 'medical', 'medical:list', 'medical:add', 'medical:edit', 'medical:detail');

-- 为患者分配权限（挂号创建、取消、查看）
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 4, id FROM `permission` WHERE permission_code IN ('registration', 'registration:create', 'registration:cancel', 'registration:detail', 'medical', 'medical:list', 'medical:detail');

-- =============================================
-- 迁移现有用户角色数据
-- =============================================
-- 将user表中role字段的值迁移到user_role表
INSERT INTO `user_role` (`user_id`, `role_id`)
SELECT u.id,
       CASE u.role
           WHEN 'SUPER_ADMIN' THEN 1
           WHEN 'ADMIN' THEN 2
           WHEN 'DOCTOR' THEN 3
           WHEN 'PATIENT' THEN 4
           ELSE 4
           END
FROM `user` u WHERE u.deleted = 0;

