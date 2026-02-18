-- AI问诊会话表
CREATE TABLE ai_consultation_session (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '会话ID',
                                         user_id BIGINT NOT NULL COMMENT '用户ID',
                                         session_type VARCHAR(20) NOT NULL COMMENT '会话类型(GUIDE-智能导诊/CONSULT-健康咨询)',
                                         title VARCHAR(100) COMMENT '会话标题',
                                         status TINYINT DEFAULT 1 COMMENT '状态(0已结束 1进行中)',
                                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
                                         INDEX idx_user_id (user_id),
                                         INDEX idx_create_time (create_time)
) COMMENT 'AI问诊会话表';

-- AI问诊消息记录表
CREATE TABLE ai_consultation_message (
                                         id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '消息ID',
                                         session_id BIGINT NOT NULL COMMENT '会话ID',
                                         role VARCHAR(20) NOT NULL COMMENT '角色(user-用户/assistant-AI)',
                                         content TEXT NOT NULL COMMENT '消息内容',
                                         recommended_department_id BIGINT COMMENT '推荐科室ID(导诊时)',
                                         recommended_doctor_id BIGINT COMMENT '推荐医生ID(导诊时)',
                                         create_time DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
                                         INDEX idx_session_id (session_id)
) COMMENT 'AI问诊消息记录表';