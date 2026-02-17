-- 消息记录表
CREATE TABLE `message_record` (
                                  `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                  `user_id` BIGINT NOT NULL COMMENT '接收用户ID',
                                  `message_type` VARCHAR(50) NOT NULL COMMENT '消息类型',
                                  `channel` VARCHAR(20) NOT NULL COMMENT '发送渠道：SMS-短信，WECHAT-微信，SYSTEM-站内',
                                  `title` VARCHAR(100) DEFAULT NULL COMMENT '消息标题',
                                  `content` TEXT NOT NULL COMMENT '消息内容',
                                  `receiver` VARCHAR(100) NOT NULL COMMENT '接收地址（手机号/openId）',
                                  `send_status` TINYINT NOT NULL DEFAULT 0 COMMENT '发送状态：0-待发送，1-已发送，2-发送失败',
                                  `send_time` DATETIME DEFAULT NULL COMMENT '发送时间',
                                  `read_status` TINYINT NOT NULL DEFAULT 0 COMMENT '阅读状态：0-未读，1-已读',
                                  `read_time` DATETIME DEFAULT NULL COMMENT '阅读时间',
                                  `error_msg` VARCHAR(500) DEFAULT NULL COMMENT '错误信息',
                                  `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                  PRIMARY KEY (`id`),
                                  KEY `idx_user_id` (`user_id`),
                                  KEY `idx_message_type` (`message_type`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息记录表';
