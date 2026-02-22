-- 支付管理
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (0, 'payment', '支付管理', 'MENU', 7, 1, NOW(), NOW(), 0);

SET @payment_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
                                                                                                                                                 (@payment_id, 'payment:list', '支付列表', 'BUTTON', 1, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@payment_id, 'payment:refund', '退款操作', 'BUTTON', 2, 1, NOW(), NOW(), 0);

-- 公告管理（放在消息管理下）
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (38, 'announcement', '公告管理', 'MENU', 3, 1, NOW(), NOW(), 0);

SET @announcement_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
                                                                                                                                                 (@announcement_id, 'announcement:list', '公告列表', 'BUTTON', 1, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@announcement_id, 'announcement:add', '新增公告', 'BUTTON', 2, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@announcement_id, 'announcement:edit', '编辑公告', 'BUTTON', 3, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@announcement_id, 'announcement:delete', '删除公告', 'BUTTON', 4, 1, NOW(), NOW(), 0);

-- 权限管理
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (1, 'system:permission', '权限管理', 'MENU', 3, 1, NOW(), NOW(), 0);

SET @permission_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
                                                                                                                                                 (@permission_id, 'system:permission:list', '权限列表', 'BUTTON', 1, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@permission_id, 'system:permission:add', '新增权限', 'BUTTON', 2, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@permission_id, 'system:permission:edit', '编辑权限', 'BUTTON', 3, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@permission_id, 'system:permission:delete', '删除权限', 'BUTTON', 4, 1, NOW(), NOW(), 0);

-- 系统配置
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (1, 'system:config', '系统配置', 'MENU', 4, 1, NOW(), NOW(), 0);

SET @config_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
                                                                                                                                                 (@config_id, 'system:config:list', '配置列表', 'BUTTON', 1, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@config_id, 'system:config:edit', '编辑配置', 'BUTTON', 2, 1, NOW(), NOW(), 0);

-- 数据字典
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (1, 'system:dict', '数据字典', 'MENU', 5, 1, NOW(), NOW(), 0);

SET @dict_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
                                                                                                                                                 (@dict_id, 'system:dict:list', '字典列表', 'BUTTON', 1, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@dict_id, 'system:dict:add', '新增字典', 'BUTTON', 2, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@dict_id, 'system:dict:edit', '编辑字典', 'BUTTON', 3, 1, NOW(), NOW(), 0),
                                                                                                                                                 (@dict_id, 'system:dict:delete', '删除字典', 'BUTTON', 4, 1, NOW(), NOW(), 0);

-- 系统日志
INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (1, 'system:log', '系统日志', 'MENU', 6, 1, NOW(), NOW(), 0);

SET @log_id = LAST_INSERT_ID();

INSERT INTO permission (parent_id, permission_code, permission_name, permission_type, sort_order, status, create_time, update_time, deleted) VALUES
    (@log_id, 'system:log:list', '日志列表', 'BUTTON', 1, 1, NOW(), NOW(), 0);