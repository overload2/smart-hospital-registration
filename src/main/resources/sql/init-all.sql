-- =============================================
-- 智慧医院挂号系统 - 完整数据库初始化脚本
-- 执行方式: mysql -u root -p < init-all.sql
-- =============================================

-- 创建数据库
CREATE DATABASE IF NOT EXISTS hospital_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE hospital_db;

-- =============================================
-- 第一部分：基础表结构
-- =============================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user` (
                                      `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                      `username` VARCHAR(50) NOT NULL UNIQUE COMMENT '用户名',
                                      `password` VARCHAR(100) NOT NULL COMMENT '密码（加密）',
                                      `real_name` VARCHAR(50) COMMENT '真实姓名',
                                      `id_card` VARCHAR(18) COMMENT '身份证号',
                                      `phone` VARCHAR(20) COMMENT '手机号',
                                      `gender` VARCHAR(10) COMMENT '性别：MALE-男，FEMALE-女',
                                      `birth_date` DATE COMMENT '出生日期',
                                      `email` VARCHAR(100) COMMENT '邮箱',
                                      `role` VARCHAR(20) NOT NULL DEFAULT 'PATIENT' COMMENT '角色：PATIENT-患者，DOCTOR-医生，ADMIN-管理员',
                                      `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                      `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      INDEX idx_username (`username`),
                                      INDEX idx_phone (`phone`),
                                      INDEX idx_deleted (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='用户表';

-- 2. 科室表
CREATE TABLE IF NOT EXISTS `department` (
                                            `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                            `name` VARCHAR(50) NOT NULL COMMENT '科室名称',
                                            `code` VARCHAR(20) NOT NULL UNIQUE COMMENT '科室编码',
                                            `description` VARCHAR(500) COMMENT '科室简介',
                                            `location` VARCHAR(100) COMMENT '科室位置',
                                            `phone` VARCHAR(20) COMMENT '科室电话',
                                            `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                            `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            INDEX idx_code (`code`),
                                            INDEX idx_deleted (`deleted`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='科室表';

-- 3. 医生表
CREATE TABLE IF NOT EXISTS `doctor` (
                                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                        `user_id` BIGINT NOT NULL COMMENT '关联用户ID',
                                        `department_id` BIGINT NOT NULL COMMENT '关联科室ID',
                                        `title` VARCHAR(50) COMMENT '职称',
                                        `specialty` VARCHAR(200) COMMENT '专长',
                                        `introduction` VARCHAR(1000) COMMENT '医生简介',
                                        `registration_fee` DECIMAL(10,2) COMMENT '挂号费',
                                        `rating` DECIMAL(3,2) DEFAULT 0.00 COMMENT '评分',
                                        `consultation_count` INT DEFAULT 0 COMMENT '接诊数',
                                        `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                        `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                        `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                        `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                        INDEX idx_user_id (`user_id`),
                                        INDEX idx_department_id (`department_id`),
                                        INDEX idx_deleted (`deleted`),
                                        FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                                        FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生表';

-- 4. 排班表
CREATE TABLE IF NOT EXISTS `schedule` (
                                          `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                          `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
                                          `department_id` BIGINT NOT NULL COMMENT '科室ID',
                                          `schedule_date` DATE NOT NULL COMMENT '排班日期',
                                          `time_slot` VARCHAR(20) NOT NULL COMMENT '时间段：MORNING/AFTERNOON/EVENING',
                                          `total_number` INT NOT NULL COMMENT '总号源数',
                                          `remaining_number` INT NOT NULL COMMENT '剩余号源数',
                                          `slot_capacity` INT DEFAULT 5 COMMENT '每个细分时段号源数',
                                          `status` INT NOT NULL DEFAULT 1 COMMENT '状态：0-已取消，1-可预约，2-已满',
                                          `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                          `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                          `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                          INDEX idx_doctor_id (`doctor_id`),
                                          INDEX idx_schedule_date (`schedule_date`),
                                          INDEX idx_deleted (`deleted`),
                                          UNIQUE KEY uk_doctor_date_slot (`doctor_id`, `schedule_date`, `time_slot`),
                                          FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE,
                                          FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='排班表';

-- 5. 挂号记录表
CREATE TABLE IF NOT EXISTS `registration` (
                                              `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                              `registration_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '挂号单号',
                                              `patient_id` BIGINT NOT NULL COMMENT '患者ID',
                                              `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
                                              `department_id` BIGINT NOT NULL COMMENT '科室ID',
                                              `schedule_id` BIGINT NOT NULL COMMENT '排班ID',
                                              `registration_date` DATE NOT NULL COMMENT '挂号日期',
                                              `time_slot` VARCHAR(20) NOT NULL COMMENT '就诊时间段',
                                              `detail_time_slot` VARCHAR(20) COMMENT '细分时段，如 M_0800',
                                              `queue_number` INT COMMENT '排队号',
                                              `registration_fee` DECIMAL(10,2) COMMENT '挂号费',
                                              `status` VARCHAR(20) NOT NULL COMMENT '状态：PENDING/CALLED/CONSULTING/COMPLETED/CANCELLED/MISSED',
                                              `symptom` VARCHAR(500) COMMENT '症状描述',
                                              `payment_status` VARCHAR(20) COMMENT '支付状态：PENDING/PAID/REFUNDED',
                                              `payment_time` DATETIME COMMENT '支付时间',
                                              `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                              `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                              INDEX idx_registration_no (`registration_no`),
                                              INDEX idx_patient_id (`patient_id`),
                                              INDEX idx_doctor_id (`doctor_id`),
                                              INDEX idx_registration_date (`registration_date`),
                                              INDEX idx_deleted (`deleted`),
                                              FOREIGN KEY (`patient_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                                              FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE,
                                              FOREIGN KEY (`department_id`) REFERENCES `department`(`id`) ON DELETE CASCADE,
                                              FOREIGN KEY (`schedule_id`) REFERENCES `schedule`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='挂号记录表';

-- 6. 支付记录表
CREATE TABLE IF NOT EXISTS `payment` (
                                         `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                         `transaction_no` VARCHAR(50) NOT NULL UNIQUE COMMENT '交易流水号',
                                         `registration_id` BIGINT NOT NULL COMMENT '挂号ID',
                                         `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                         `amount` DECIMAL(10,2) NOT NULL COMMENT '支付金额',
                                         `payment_method` VARCHAR(20) COMMENT '支付方式：ALIPAY/WECHAT/CASH',
                                         `payment_status` VARCHAR(20) NOT NULL COMMENT '支付状态：PENDING/PAID/REFUNDED',
                                         `payment_time` DATETIME COMMENT '支付时间',
                                         `refund_time` DATETIME COMMENT '退款时间',
                                         `remark` VARCHAR(500) COMMENT '备注',
                                         `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                         `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         INDEX idx_transaction_no (`transaction_no`),
                                         INDEX idx_registration_id (`registration_id`),
                                         INDEX idx_deleted (`deleted`),
                                         FOREIGN KEY (`registration_id`) REFERENCES `registration`(`id`) ON DELETE CASCADE,
                                         FOREIGN KEY (`user_id`) REFERENCES `user`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='支付记录表';

-- 7. 就诊记录表（病历）
CREATE TABLE IF NOT EXISTS `medical_record` (
                                                `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                                `registration_id` BIGINT NOT NULL COMMENT '挂号ID',
                                                `patient_id` BIGINT NOT NULL COMMENT '患者ID',
                                                `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
                                                `chief_complaint` VARCHAR(500) COMMENT '主诉',
                                                `present_illness` TEXT COMMENT '现病史',
                                                `past_history` TEXT COMMENT '既往史',
                                                `allergy_history` TEXT COMMENT '过敏史',
                                                `diagnosis` VARCHAR(1000) COMMENT '诊断结果',
                                                `prescription` VARCHAR(2000) COMMENT '处方',
                                                `advice` VARCHAR(1000) COMMENT '医嘱',
                                                `visit_time` DATETIME COMMENT '就诊时间',
                                                `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                INDEX idx_registration_id (`registration_id`),
                                                INDEX idx_patient_id (`patient_id`),
                                                INDEX idx_doctor_id (`doctor_id`),
                                                INDEX idx_deleted (`deleted`),
                                                FOREIGN KEY (`registration_id`) REFERENCES `registration`(`id`) ON DELETE CASCADE,
                                                FOREIGN KEY (`patient_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                                                FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='就诊记录表';

-- 8. 医生评价表
CREATE TABLE IF NOT EXISTS `doctor_evaluation` (
                                                   `id` BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                                   `doctor_id` BIGINT NOT NULL COMMENT '医生ID',
                                                   `patient_id` BIGINT NOT NULL COMMENT '患者ID',
                                                   `registration_id` BIGINT NOT NULL COMMENT '挂号ID',
                                                   `rating` INT NOT NULL COMMENT '评分（1-5分）',
                                                   `comment` VARCHAR(1000) COMMENT '评价内容',
                                                   `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                                   `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                   `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                   INDEX idx_doctor_id (`doctor_id`),
                                                   INDEX idx_deleted (`deleted`),
                                                   FOREIGN KEY (`doctor_id`) REFERENCES `doctor`(`id`) ON DELETE CASCADE,
                                                   FOREIGN KEY (`patient_id`) REFERENCES `user`(`id`) ON DELETE CASCADE,
                                                   FOREIGN KEY (`registration_id`) REFERENCES `registration`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci COMMENT='医生评价表';

-- =============================================
-- 第二部分：RBAC权限管理表
-- =============================================

-- 9. 角色表
CREATE TABLE IF NOT EXISTS `role` (
                                      `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                      `role_code` VARCHAR(50) NOT NULL COMMENT '角色编码',
                                      `role_name` VARCHAR(50) NOT NULL COMMENT '角色名称',
                                      `description` VARCHAR(200) DEFAULT NULL COMMENT '角色描述',
                                      `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                      `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                      `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                      `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                      UNIQUE KEY `uk_role_code` (`role_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色表';

-- 10. 权限表
CREATE TABLE IF NOT EXISTS `permission` (
                                            `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                            `parent_id` BIGINT DEFAULT 0 COMMENT '父权限ID（0表示顶级）',
                                            `permission_code` VARCHAR(100) NOT NULL COMMENT '权限编码',
                                            `permission_name` VARCHAR(50) NOT NULL COMMENT '权限名称',
                                            `permission_type` VARCHAR(20) NOT NULL COMMENT '权限类型：MENU-菜单，BUTTON-按钮',
                                            `path` VARCHAR(200) DEFAULT NULL COMMENT '路由路径',
                                            `icon` VARCHAR(50) DEFAULT NULL COMMENT '图标',
                                            `sort_order` INT DEFAULT 0 COMMENT '排序号',
                                            `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                            `deleted` TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除：0-未删除，1-已删除',
                                            `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            UNIQUE KEY `uk_permission_code` (`permission_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='权限表';

-- 11. 用户角色关联表
CREATE TABLE IF NOT EXISTS `user_role` (
                                           `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                           `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                           `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                           `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                           UNIQUE KEY `uk_user_role` (`user_id`, `role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户角色关联表';

-- 12. 角色权限关联表
CREATE TABLE IF NOT EXISTS `role_permission` (
                                                 `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                                 `role_id` BIGINT NOT NULL COMMENT '角色ID',
                                                 `permission_id` BIGINT NOT NULL COMMENT '权限ID',
                                                 `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                 UNIQUE KEY `uk_role_permission` (`role_id`, `permission_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='角色权限关联表';

-- =============================================
-- 第三部分：系统管理表
-- =============================================

-- 13. 系统配置表
CREATE TABLE IF NOT EXISTS `sys_config` (
                                            `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
                                            `config_key` VARCHAR(100) NOT NULL COMMENT '配置键',
                                            `config_value` VARCHAR(500) NOT NULL COMMENT '配置值',
                                            `config_name` VARCHAR(100) NOT NULL COMMENT '配置名称',
                                            `config_type` VARCHAR(50) DEFAULT 'STRING' COMMENT '配置类型',
                                            `remark` VARCHAR(255) COMMENT '备注',
                                            `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                            `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                            UNIQUE KEY uk_config_key (`config_key`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统配置表';

-- 14. 操作日志表
CREATE TABLE IF NOT EXISTS `sys_operation_log` (
                                                   `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                                                   `user_id` BIGINT COMMENT '操作用户ID',
                                                   `username` VARCHAR(50) COMMENT '操作用户名',
                                                   `module` VARCHAR(50) NOT NULL COMMENT '操作模块',
                                                   `operation` VARCHAR(50) NOT NULL COMMENT '操作类型',
                                                   `method` VARCHAR(200) COMMENT '请求方法',
                                                   `params` TEXT COMMENT '请求参数',
                                                   `ip` VARCHAR(50) COMMENT '操作IP',
                                                   `status` TINYINT DEFAULT 1 COMMENT '操作状态',
                                                   `error_msg` TEXT COMMENT '错误信息',
                                                   `operation_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                                   `cost_time` BIGINT COMMENT '耗时(毫秒)',
                                                   INDEX idx_user_id (`user_id`),
                                                   INDEX idx_operation_time (`operation_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='操作日志表';

-- 15. 数据字典类型表
CREATE TABLE IF NOT EXISTS `sys_dict_type` (
                                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典类型ID',
                                               `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
                                               `dict_name` VARCHAR(100) NOT NULL COMMENT '字典类型名称',
                                               `status` TINYINT DEFAULT 1 COMMENT '状态',
                                               `remark` VARCHAR(255) COMMENT '备注',
                                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                               `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                               UNIQUE KEY uk_dict_type (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典类型表';

-- 16. 数据字典数据表
CREATE TABLE IF NOT EXISTS `sys_dict_data` (
                                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典数据ID',
                                               `dict_type` VARCHAR(100) NOT NULL COMMENT '字典类型编码',
                                               `dict_label` VARCHAR(100) NOT NULL COMMENT '字典标签',
                                               `dict_value` VARCHAR(100) NOT NULL COMMENT '字典值',
                                               `sort_order` INT DEFAULT 0 COMMENT '排序',
                                               `status` TINYINT DEFAULT 1 COMMENT '状态',
                                               `remark` VARCHAR(255) COMMENT '备注',
                                               `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                               `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                               INDEX idx_dict_type (`dict_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据字典数据表';

-- 17. 登录日志表
CREATE TABLE IF NOT EXISTS `sys_login_log` (
                                               `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                                               `user_id` BIGINT COMMENT '用户ID',
                                               `username` VARCHAR(50) COMMENT '用户名',
                                               `ip` VARCHAR(50) COMMENT '登录IP',
                                               `location` VARCHAR(100) COMMENT '登录地点',
                                               `browser` VARCHAR(50) COMMENT '浏览器',
                                               `os` VARCHAR(50) COMMENT '操作系统',
                                               `status` TINYINT DEFAULT 1 COMMENT '登录状态',
                                               `msg` VARCHAR(255) COMMENT '提示消息',
                                               `login_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
                                               INDEX idx_user_id (`user_id`),
                                               INDEX idx_login_time (`login_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='登录日志表';

-- =============================================
-- 第四部分：消息与公告表
-- =============================================

-- 18. 消息记录表
CREATE TABLE IF NOT EXISTS `message_record` (
                                                `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                                `template_id` BIGINT COMMENT '消息模板ID',
                                                `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
                                                `message_type` VARCHAR(50) NOT NULL COMMENT '消息类型',
                                                `channel` VARCHAR(20) NOT NULL COMMENT '发送渠道：SMS/WECHAT/SYSTEM',
                                                `title` VARCHAR(100) DEFAULT NULL COMMENT '消息标题',
                                                `content` TEXT NOT NULL COMMENT '消息内容',
                                                `receiver` VARCHAR(100) NOT NULL COMMENT '接收地址',
                                                `send_status` TINYINT NOT NULL DEFAULT 0 COMMENT '发送状态：0-待发送，1-已发送，2-发送失败',
                                                `send_time` DATETIME DEFAULT NULL COMMENT '发送时间',
                                                `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
                                                `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
                                                `error_msg` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
                                                `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                INDEX idx_user_id (`user_id`),
                                                INDEX idx_message_type (`message_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息记录表';

-- 19. 消息模板表
CREATE TABLE IF NOT EXISTS `message_template` (
                                                  `id` BIGINT NOT NULL AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
                                                  `template_code` VARCHAR(50) NOT NULL COMMENT '模板编码',
                                                  `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
                                                  `message_type` VARCHAR(50) NOT NULL COMMENT '消息类型',
                                                  `channel` VARCHAR(20) NOT NULL COMMENT '发送渠道',
                                                  `title_template` VARCHAR(100) DEFAULT NULL COMMENT '标题模板',
                                                  `content_template` TEXT NOT NULL COMMENT '内容模板',
                                                  `variables` VARCHAR(500) DEFAULT NULL COMMENT '变量说明',
                                                  `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态',
                                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                  `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                  UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- 20. 公告表
CREATE TABLE IF NOT EXISTS `announcement` (
                                              `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键',
                                              `title` VARCHAR(200) NOT NULL COMMENT '公告标题',
                                              `content` TEXT COMMENT '公告内容',
                                              `status` TINYINT DEFAULT 1 COMMENT '状态：0-下架，1-发布',
                                              `deleted` TINYINT DEFAULT 0 COMMENT '逻辑删除',
                                              `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                              `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='系统公告表';

-- =============================================
-- 第五部分：AI问诊表
-- =============================================

-- 21. AI问诊会话表
CREATE TABLE IF NOT EXISTS `ai_consultation_session` (
                                                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
                                                         `user_id` BIGINT NOT NULL COMMENT '用户ID',
                                                         `session_type` VARCHAR(20) NOT NULL COMMENT '会话类型',
                                                         `title` VARCHAR(100) COMMENT '会话标题',
                                                         `status` TINYINT DEFAULT 1 COMMENT '状态',
                                                         `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                         `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                                         INDEX idx_user_id (`user_id`),
                                                         INDEX idx_create_time (`create_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI问诊会话表';

-- 22. AI问诊消息记录表
CREATE TABLE IF NOT EXISTS `ai_consultation_message` (
                                                         `id` BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
                                                         `session_id` BIGINT NOT NULL COMMENT '会话ID',
                                                         `role` VARCHAR(20) NOT NULL COMMENT '角色',
                                                         `content` TEXT NOT NULL COMMENT '消息内容',
                                                         `recommended_department_id` BIGINT COMMENT '推荐科室ID',
                                                         `recommended_doctor_id` BIGINT COMMENT '推荐医生ID',
                                                         `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                                         INDEX idx_session_id (`session_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='AI问诊消息记录表';

-- =============================================
-- 第六部分：初始化数据
-- =============================================

-- 初始化用户（密码都是123456）
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `gender`, `role`, `status`) VALUES
                                                                                                  ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '超级管理员', '13800000000', 'MALE', 'ADMIN', 1),
                                                                                                  ('doctor_wang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王医生', '13800000001', 'MALE', 'DOCTOR', 1),
                                                                                                  ('doctor_li', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李医生', '13800000002', 'FEMALE', 'DOCTOR', 1),
                                                                                                  ('patient_zhang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张患者', '13900000001', 'MALE', 'PATIENT', 1),
                                                                                                  ('user', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '普通用户', '13900000002', 'MALE', 'USER', 1);

-- 初始化科室
INSERT INTO `department` (`name`, `code`, `description`, `location`, `phone`) VALUES
                                                                                  ('内科', 'NEIKE', '内科诊疗服务', '1号楼2楼', '0571-12345601'),
                                                                                  ('外科', 'WAIKE', '外科诊疗服务', '1号楼3楼', '0571-12345602'),
                                                                                  ('儿科', 'ERKE', '儿科诊疗服务', '2号楼1楼', '0571-12345603'),
                                                                                  ('妇产科', 'FUCHANKEKE', '妇产科诊疗服务', '2号楼2楼', '0571-12345604');

-- 初始化医生
INSERT INTO `doctor` (`user_id`, `department_id`, `title`, `specialty`, `introduction`, `registration_fee`, `rating`) VALUES
                                                                                                                          (2, 1, '主任医师', '心血管疾病、高血压', '从事内科临床工作20年，擅长心血管疾病诊治', 50.00, 4.8),
                                                                                                                          (3, 3, '副主任医师', '儿童呼吸道疾病、过敏性疾病', '从事儿科临床工作15年，对儿童常见病有丰富经验', 30.00, 4.5);

-- 初始化角色
INSERT INTO `role` (`role_code`, `role_name`, `description`) VALUES
                                                                 ('SUPER_ADMIN', '超级管理员', '拥有系统所有权限'),
                                                                 ('ADMIN', '管理员', '管理科室、医生、排班等基础数据'),
                                                                 ('DOCTOR', '医生', '查看接诊列表、填写病历'),
                                                                 ('PATIENT', '患者', '挂号预约、查询病历、评价医生'),
                                                                 ('USER', '普通用户', '基础用户权限');

-- =============================================
-- 初始化权限数据
-- =============================================

INSERT INTO `permission` (`id`, `parent_id`, `permission_code`, `permission_name`, `permission_type`, `path`, `icon`, `sort_order`, `status`, `deleted`) VALUES
                                                                                                                                                             -- 系统管理
                                                                                                                                                             (1, 0, 'system', '系统管理', 'MENU', '', '', 1, 1, 0),
                                                                                                                                                             (2, 1, 'system:user', '用户管理', 'MENU', '', '', 1, 1, 0),
                                                                                                                                                             (3, 2, 'system:user:list', '用户列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (4, 2, 'system:user:add', '新增用户', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (5, 2, 'system:user:edit', '编辑用户', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (6, 2, 'system:user:delete', '删除用户', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             (7, 1, 'system:role', '角色管理', 'MENU', '', '', 2, 1, 0),
                                                                                                                                                             (8, 7, 'system:role:list', '角色列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (9, 7, 'system:role:add', '新增角色', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (10, 7, 'system:role:edit', '编辑角色', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (11, 7, 'system:role:delete', '删除角色', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             (12, 7, 'system:role:assign', '分配权限', 'BUTTON', '', '', 5, 1, 0),
                                                                                                                                                             -- 科室管理
                                                                                                                                                             (13, 0, 'department', '科室管理', 'MENU', '', '', 2, 1, 0),
                                                                                                                                                             (14, 13, 'department:list', '科室列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (15, 13, 'department:add', '新增科室', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (16, 13, 'department:edit', '编辑科室', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (17, 13, 'department:delete', '删除科室', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 医生管理
                                                                                                                                                             (18, 0, 'doctor', '医生管理', 'MENU', '', '', 3, 1, 0),
                                                                                                                                                             (19, 18, 'doctor:list', '医生列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (20, 18, 'doctor:add', '新增医生', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (21, 18, 'doctor:edit', '编辑医生', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (22, 18, 'doctor:delete', '删除医生', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 排班管理
                                                                                                                                                             (23, 0, 'schedule', '排班管理', 'MENU', '', '', 4, 1, 0),
                                                                                                                                                             (24, 23, 'schedule:list', '排班列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (25, 23, 'schedule:add', '新增排班', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (26, 23, 'schedule:edit', '编辑排班', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (27, 23, 'schedule:delete', '删除排班', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 挂号管理
                                                                                                                                                             (28, 0, 'registration', '挂号管理', 'MENU', '', '', 5, 1, 0),
                                                                                                                                                             (29, 28, 'registration:list', '挂号列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (30, 28, 'registration:create', '创建挂号', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (31, 28, 'registration:cancel', '取消挂号', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (32, 28, 'registration:detail', '挂号详情', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 病历管理
                                                                                                                                                             (33, 0, 'medical', '病历管理', 'MENU', '', '', 6, 1, 0),
                                                                                                                                                             (34, 33, 'medical:list', '病历列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (35, 33, 'medical:add', '新增病历', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (36, 33, 'medical:edit', '编辑病历', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (37, 33, 'medical:detail', '病历详情', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 消息管理（父级）
                                                                                                                                                             (38, 0, 'message', '消息管理', 'MENU', '/message', 'Bell', 50, 1, 0),
                                                                                                                                                             (39, 38, 'message:record', '消息记录', 'MENU', '/message', 'Message', 1, 1, 0),
                                                                                                                                                             (40, 39, 'message:list', '查看消息', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (41, 39, 'message:send', '发送消息', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (42, 39, 'message:delete', '删除消息', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (43, 38, 'message:template', '消息模板', 'MENU', '/message/template', 'Document', 2, 1, 0),
                                                                                                                                                             (44, 43, 'message:template:add', '新增模板', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (45, 43, 'message:template:edit', '编辑模板', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (46, 43, 'message:template:delete', '删除模板', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             -- 支付管理
                                                                                                                                                             (47, 0, 'payment', '支付管理', 'MENU', '', '', 7, 1, 0),
                                                                                                                                                             (48, 47, 'payment:list', '支付列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (49, 47, 'payment:refund', '退款操作', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             -- 公告管理（在消息管理下）
                                                                                                                                                             (50, 38, 'announcement', '公告管理', 'MENU', '', '', 3, 1, 0),
                                                                                                                                                             (51, 50, 'announcement:list', '公告列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (52, 50, 'announcement:add', '新增公告', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (53, 50, 'announcement:edit', '编辑公告', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (54, 50, 'announcement:delete', '删除公告', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 权限管理（在系统管理下）
                                                                                                                                                             (55, 1, 'system:permission', '权限管理', 'MENU', '', '', 3, 1, 0),
                                                                                                                                                             (56, 55, 'system:permission:list', '权限列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (57, 55, 'system:permission:add', '新增权限', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (58, 55, 'system:permission:edit', '编辑权限', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (59, 55, 'system:permission:delete', '删除权限', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 系统配置（在系统管理下）
                                                                                                                                                             (60, 1, 'system:config', '系统配置', 'MENU', '', '', 4, 1, 0),
                                                                                                                                                             (61, 60, 'system:config:list', '配置列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (62, 60, 'system:config:edit', '编辑配置', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             -- 数据字典（在系统管理下）
                                                                                                                                                             (63, 1, 'system:dict', '数据字典', 'MENU', '', '', 5, 1, 0),
                                                                                                                                                             (64, 63, 'system:dict:list', '字典列表', 'BUTTON', '', '', 1, 1, 0),
                                                                                                                                                             (65, 63, 'system:dict:add', '新增字典', 'BUTTON', '', '', 2, 1, 0),
                                                                                                                                                             (66, 63, 'system:dict:edit', '编辑字典', 'BUTTON', '', '', 3, 1, 0),
                                                                                                                                                             (67, 63, 'system:dict:delete', '删除字典', 'BUTTON', '', '', 4, 1, 0),
                                                                                                                                                             -- 系统日志（在系统管理下）
                                                                                                                                                             (68, 1, 'system:log', '系统日志', 'MENU', '', '', 6, 1, 0),
                                                                                                                                                             (69, 68, 'system:log:list', '日志列表', 'BUTTON', '', '', 1, 1, 0);

-- 用户角色关联
INSERT INTO `user_role` (`user_id`, `role_id`) VALUES
                                                   (1, 1),  -- admin -> 超级管理员
                                                   (2, 3),  -- doctor_wang -> 医生
                                                   (3, 3),  -- doctor_li -> 医生
                                                   (4, 4),  -- patient_zhang -> 患者
                                                   (5, 5);  -- user -> 普通用户

-- 超级管理员拥有所有权限
INSERT INTO `role_permission` (`role_id`, `permission_id`)
SELECT 1, id FROM `permission`;



-- 初始化系统配置
INSERT INTO `sys_config` (`config_key`, `config_value`, `config_name`, `config_type`, `remark`) VALUES
                                                                                                    ('hospital_name', '智慧医院', '医院名称', 'STRING', '系统显示的医院名称'),
                                                                                                    ('max_registration_per_slot', '30', '每时段最大挂号数', 'NUMBER', '每个医生每个时段的最大挂号数量'),
                                                                                                    ('advance_booking_days', '7', '可提前预约天数', 'NUMBER', '患者可以提前多少天预约挂号'),
                                                                                                    ('cancel_before_hours', '2', '取消预约提前小时数', 'NUMBER', '需要提前多少小时才能取消预约'),
                                                                                                    ('registration_fee_normal', '10.00', '普通号挂号费', 'NUMBER', '普通门诊挂号费用'),
                                                                                                    ('registration_fee_expert', '50.00', '专家号挂号费', 'NUMBER', '专家门诊挂号费用');

-- 初始化数据字典类型
INSERT INTO `sys_dict_type` (`dict_type`, `dict_name`, `remark`) VALUES
                                                                     ('payment_method', '支付方式', '挂号支付方式'),
                                                                     ('registration_status', '挂号状态', '挂号记录状态'),
                                                                     ('schedule_type', '排班类型', '医生排班类型'),
                                                                     ('gender', '性别', '用户性别');

-- 初始化数据字典数据
INSERT INTO `sys_dict_data` (`dict_type`, `dict_label`, `dict_value`, `sort_order`) VALUES
                                                                                        ('payment_method', '微信支付', 'WECHAT', 1),
                                                                                        ('payment_method', '支付宝', 'ALIPAY', 2),
                                                                                        ('payment_method', '银行卡', 'BANK_CARD', 3),
                                                                                        ('payment_method', '现金', 'CASH', 4),
                                                                                        ('registration_status', '待支付', 'PENDING', 1),
                                                                                        ('registration_status', '已支付', 'PAID', 2),
                                                                                        ('registration_status', '已取消', 'CANCELLED', 3),
                                                                                        ('registration_status', '已完成', 'COMPLETED', 4),
                                                                                        ('schedule_type', '普通门诊', 'NORMAL', 1),
                                                                                        ('schedule_type', '专家门诊', 'EXPERT', 2),
                                                                                        ('gender', '男', 'MALE', 1),
                                                                                        ('gender', '女', 'FEMALE', 2);

-- 初始化消息模板
INSERT INTO `message_template` (`template_code`, `template_name`, `message_type`, `channel`, `title_template`, `content_template`, `variables`) VALUES
                                                                                                                                                    ('REG_SUCCESS', '挂号成功通知', 'REGISTRATION_SUCCESS', 'SYSTEM', '挂号成功通知', '【智慧医院】您已成功预约挂号。挂号单号：{registrationNo}，就诊日期：{visitDate} {timeSlot}，科室：{departmentName}，医生：{doctorName}，排队号：{queueNumber}，挂号费：{fee}元。请按时就诊！', '{"registrationNo":"挂号单号","visitDate":"就诊日期","timeSlot":"时间段","departmentName":"科室名称","doctorName":"医生姓名","queueNumber":"排队号","fee":"挂号费"}'),
                                                                                                                                                    ('PAY_SUCCESS', '支付成功通知', 'PAYMENT_SUCCESS', 'SYSTEM', '支付成功通知', '【智慧医院】您的挂号费已支付成功。支付金额：{amount}元，支付方式：{paymentMethod}，挂号单号：{registrationNo}。请按时就诊！', '{"amount":"支付金额","paymentMethod":"支付方式","registrationNo":"挂号单号"}'),
                                                                                                                                                    ('REG_CANCEL', '挂号取消通知', 'REGISTRATION_CANCEL', 'SYSTEM', '挂号取消通知', '【智慧医院】您的挂号已取消。挂号单号：{registrationNo}，原就诊日期：{visitDate}，科室：{departmentName}，医生：{doctorName}。如已支付，退款将在1-3个工作日内到账。', '{"registrationNo":"挂号单号","visitDate":"就诊日期","departmentName":"科室名称","doctorName":"医生姓名"}'),
                                                                                                                                                    ('REFUND_SUCCESS', '退款成功通知', 'REFUND_SUCCESS', 'SYSTEM', '退款成功通知', '【智慧医院】您的挂号费已退款成功。退款金额：{amount}元，交易流水号：{transactionNo}。退款已原路返回，请注意查收。', '{"amount":"退款金额","transactionNo":"交易流水号"}'),
                                                                                                                                                    ('VISIT_REMINDER', '就诊提醒', 'VISIT_REMINDER', 'SYSTEM', '就诊提醒', '【智慧医院】温馨提醒：您预约的{visitDate} {timeSlot} {departmentName} {doctorName}门诊即将开始，请提前到达医院取号候诊。', '{"visitDate":"就诊日期","timeSlot":"时间段","departmentName":"科室名称","doctorName":"医生姓名"}'),
                                                                                                                                                    ('QUEUE_CALL', '排队叫号', 'QUEUE_CALL', 'SYSTEM', '叫号通知', '【智慧医院】{patientName}您好，您的排队号{queueNumber}即将叫号，请前往{departmentName}候诊区等候。', '{"patientName":"患者姓名","queueNumber":"排队号","departmentName":"科室名称"}');

-- 初始化公告
INSERT INTO `announcement` (`title`, `content`, `status`) VALUES
                                                              ('欢迎使用智慧医院挂号系统', '智慧医院挂号系统正式上线，为您提供便捷的在线挂号服务。', 1),
                                                              ('春节期间门诊安排通知', '春节期间（除夕至初六）门诊正常开放，急诊24小时服务。', 1);