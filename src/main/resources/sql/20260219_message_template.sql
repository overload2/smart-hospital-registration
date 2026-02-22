-- 消息模板表
CREATE TABLE `message_template` (
                                    `id` BIGINT NOT NULL AUTO_INCREMENT COMMENT '主键ID',
                                    `template_code` VARCHAR(50) NOT NULL COMMENT '模板编码',
                                    `template_name` VARCHAR(100) NOT NULL COMMENT '模板名称',
                                    `message_type` VARCHAR(50) NOT NULL COMMENT '消息类型',
                                    `channel` VARCHAR(20) NOT NULL COMMENT '发送渠道：SMS-短信，WECHAT-微信，SYSTEM-站内',
                                    `title_template` VARCHAR(100) DEFAULT NULL COMMENT '标题模板',
                                    `content_template` TEXT NOT NULL COMMENT '内容模板（支持变量如{patientName}）',
                                    `variables` VARCHAR(500) DEFAULT NULL COMMENT '变量说明（JSON格式）',
                                    `status` TINYINT NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
                                    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                    `update_time` DATETIME DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                    PRIMARY KEY (`id`),
                                    UNIQUE KEY `uk_template_code` (`template_code`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='消息模板表';

-- 初始化模板数据
INSERT INTO `message_template` (`template_code`, `template_name`, `message_type`, `channel`, `title_template`, `content_template`, `variables`) VALUES
                                                                                                                                                    ('REG_SUCCESS', '挂号成功通知', 'REGISTRATION_SUCCESS', 'SYSTEM', '挂号成功通知', '【智慧医院】您已成功预约挂号。挂号单号：{registrationNo}，就诊日期：{visitDate} {timeSlot}，科室：{departmentName}，医生：{doctorName}，排队号：{queueNumber}，挂号费：{fee}元。请按时就诊！', '{"registrationNo":"挂号单号","visitDate":"就诊日期","timeSlot":"时间段","departmentName":"科室名称","doctorName":"医生姓名","queueNumber":"排队号","fee":"挂号费"}'),
                                                                                                                                                    ('PAY_SUCCESS', '支付成功通知', 'PAYMENT_SUCCESS', 'SYSTEM', '支付成功通知', '【智慧医院】您的挂号费已支付成功。支付金额：{amount}元，支付方式：{paymentMethod}，挂号单号：{registrationNo}。请按时就诊！', '{"amount":"支付金额","paymentMethod":"支付方式","registrationNo":"挂号单号"}'),
                                                                                                                                                    ('REG_CANCEL', '挂号取消通知', 'REGISTRATION_CANCEL', 'SYSTEM', '挂号取消通知', '【智慧医院】您的挂号已取消。挂号单号：{registrationNo}，原就诊日期：{visitDate}，科室：{departmentName}，医生：{doctorName}。如已支付，退款将在1-3个工作日内到账。', '{"registrationNo":"挂号单号","visitDate":"就诊日期","departmentName":"科室名称","doctorName":"医生姓名"}'),
                                                                                                                                                    ('REFUND_SUCCESS', '退款成功通知', 'REFUND_SUCCESS', 'SYSTEM', '退款成功通知', '【智慧医院】您的挂号费已退款成功。退款金额：{amount}元，交易流水号：{transactionNo}。退款已原路返回，请注意查收。', '{"amount":"退款金额","transactionNo":"交易流水号"}'),
                                                                                                                                                    ('VISIT_REMINDER', '就诊提醒', 'VISIT_REMINDER', 'SYSTEM', '就诊提醒', '【智慧医院】温馨提醒：您预约的{visitDate} {timeSlot} {departmentName} {doctorName}门诊即将开始，请提前到达医院取号候诊。', '{"visitDate":"就诊日期","timeSlot":"时间段","departmentName":"科室名称","doctorName":"医生姓名"}'),
                                                                                                                                                    ('QUEUE_CALL', '排队叫号', 'QUEUE_CALL', 'SYSTEM', '叫号通知', '【智慧医院】{patientName}您好，您的排队号{queueNumber}即将叫号，请前往{departmentName}候诊区等候。', '{"patientName":"患者姓名","queueNumber":"排队号","departmentName":"科室名称"}');