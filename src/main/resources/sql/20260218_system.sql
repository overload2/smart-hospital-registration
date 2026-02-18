-- 系统配置表
CREATE TABLE sys_config (
                            id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '配置ID',
                            config_key VARCHAR(100) NOT NULL COMMENT '配置键',
                            config_value VARCHAR(500) NOT NULL COMMENT '配置值',
                            config_name VARCHAR(100) NOT NULL COMMENT '配置名称',
                            config_type VARCHAR(50) DEFAULT 'STRING' COMMENT '配置类型(STRING/NUMBER/BOOLEAN/JSON)',
                            remark VARCHAR(255) COMMENT '备注',
                            create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                            update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                            UNIQUE KEY uk_config_key (config_key)
) COMMENT '系统配置表';

-- 操作日志表
CREATE TABLE sys_operation_log (
                                   id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                                   user_id BIGINT COMMENT '操作用户ID',
                                   username VARCHAR(50) COMMENT '操作用户名',
                                   module VARCHAR(50) NOT NULL COMMENT '操作模块',
                                   operation VARCHAR(50) NOT NULL COMMENT '操作类型(ADD/UPDATE/DELETE/QUERY)',
                                   method VARCHAR(200) COMMENT '请求方法',
                                   params TEXT COMMENT '请求参数',
                                   ip VARCHAR(50) COMMENT '操作IP',
                                   status TINYINT DEFAULT 1 COMMENT '操作状态(0失败 1成功)',
                                   error_msg TEXT COMMENT '错误信息',
                                   operation_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '操作时间',
                                   cost_time BIGINT COMMENT '耗时(毫秒)',
                                   INDEX idx_user_id (user_id),
                                   INDEX idx_operation_time (operation_time)
) COMMENT '操作日志表';

-- 数据字典类型表
CREATE TABLE sys_dict_type (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典类型ID',
                               dict_type VARCHAR(100) NOT NULL COMMENT '字典类型编码',
                               dict_name VARCHAR(100) NOT NULL COMMENT '字典类型名称',
                               status TINYINT DEFAULT 1 COMMENT '状态(0禁用 1启用)',
                               remark VARCHAR(255) COMMENT '备注',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               UNIQUE KEY uk_dict_type (dict_type)
) COMMENT '数据字典类型表';

-- 数据字典数据表
CREATE TABLE sys_dict_data (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '字典数据ID',
                               dict_type VARCHAR(100) NOT NULL COMMENT '字典类型编码',
                               dict_label VARCHAR(100) NOT NULL COMMENT '字典标签',
                               dict_value VARCHAR(100) NOT NULL COMMENT '字典值',
                               sort_order INT DEFAULT 0 COMMENT '排序',
                               status TINYINT DEFAULT 1 COMMENT '状态(0禁用 1启用)',
                               remark VARCHAR(255) COMMENT '备注',
                               create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                               update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                               INDEX idx_dict_type (dict_type)
) COMMENT '数据字典数据表';

-- 登录日志表
CREATE TABLE sys_login_log (
                               id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '日志ID',
                               user_id BIGINT COMMENT '用户ID',
                               username VARCHAR(50) COMMENT '用户名',
                               ip VARCHAR(50) COMMENT '登录IP',
                               location VARCHAR(100) COMMENT '登录地点',
                               browser VARCHAR(50) COMMENT '浏览器',
                               os VARCHAR(50) COMMENT '操作系统',
                               status TINYINT DEFAULT 1 COMMENT '登录状态(0失败 1成功)',
                               msg VARCHAR(255) COMMENT '提示消息',
                               login_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '登录时间',
                               INDEX idx_user_id (user_id),
                               INDEX idx_login_time (login_time)
) COMMENT '登录日志表';

-- 初始化系统配置数据
INSERT INTO sys_config (config_key, config_value, config_name, config_type, remark) VALUES
                                                                                        ('hospital_name', '智慧医院', '医院名称', 'STRING', '系统显示的医院名称'),
                                                                                        ('max_registration_per_slot', '30', '每时段最大挂号数', 'NUMBER', '每个医生每个时段的最大挂号数量'),
                                                                                        ('advance_booking_days', '7', '可提前预约天数', 'NUMBER', '患者可以提前多少天预约挂号'),
                                                                                        ('cancel_before_hours', '2', '取消预约提前小时数', 'NUMBER', '需要提前多少小时才能取消预约'),
                                                                                        ('registration_fee_normal', '10.00', '普通号挂号费', 'NUMBER', '普通门诊挂号费用'),
                                                                                        ('registration_fee_expert', '50.00', '专家号挂号费', 'NUMBER', '专家门诊挂号费用');

-- 初始化数据字典类型
INSERT INTO sys_dict_type (dict_type, dict_name, remark) VALUES
                                                             ('payment_method', '支付方式', '挂号支付方式'),
                                                             ('registration_status', '挂号状态', '挂号记录状态'),
                                                             ('schedule_type', '排班类型', '医生排班类型'),
                                                             ('gender', '性别', '用户性别');

-- 初始化数据字典数据
INSERT INTO sys_dict_data (dict_type, dict_label, dict_value, sort_order) VALUES
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
