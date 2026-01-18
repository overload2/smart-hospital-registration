-- ====================================
-- 智能医院挂号系统 - 数据库表结构
-- ====================================

-- 1. 用户表
CREATE TABLE IF NOT EXISTS `user`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `username`    VARCHAR(50)  NOT NULL UNIQUE COMMENT '用户名',
    `password`    VARCHAR(100) NOT NULL COMMENT '密码（加密）',
    `real_name`   VARCHAR(50) COMMENT '真实姓名',
    `id_card`     VARCHAR(18) COMMENT '身份证号',
    `phone`       VARCHAR(20) COMMENT '手机号',
    `gender`      VARCHAR(10) COMMENT '性别：MALE-男，FEMALE-女',
    `birth_date`  DATE COMMENT '出生日期',
    `email`       VARCHAR(100) COMMENT '邮箱',
    `role`        VARCHAR(20)  NOT NULL DEFAULT 'PATIENT' COMMENT '角色：PATIENT-患者，DOCTOR-医生，ADMIN-管理员',
    `status`      INT          NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_username (`username`),
    INDEX idx_phone (`phone`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='用户表';

-- 2. 科室表
CREATE TABLE IF NOT EXISTS `department`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `name`        VARCHAR(50) NOT NULL COMMENT '科室名称',
    `code`        VARCHAR(20) NOT NULL UNIQUE COMMENT '科室编码',
    `description` VARCHAR(500) COMMENT '科室简介',
    `location`    VARCHAR(100) COMMENT '科室位置',
    `phone`       VARCHAR(20) COMMENT '科室电话',
    `status`      INT         NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_code (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='科室表';

-- 3. 医生表
CREATE TABLE IF NOT EXISTS `doctor`
(
    `id`                 BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `user_id`            BIGINT   NOT NULL COMMENT '关联用户ID',
    `department_id`      BIGINT   NOT NULL COMMENT '关联科室ID',
    `title`              VARCHAR(50) COMMENT '职称：主任医师/副主任医师/主治医师/住院医师',
    `specialty`          VARCHAR(200) COMMENT '专长',
    `introduction`       VARCHAR(1000) COMMENT '医生简介',
    `registration_fee`   DECIMAL(10, 2) COMMENT '挂号费',
    `rating`             DECIMAL(3, 2)     DEFAULT 0.00 COMMENT '评分（0-5分）',
    `consultation_count` INT               DEFAULT 0 COMMENT '接诊数',
    `status`             INT      NOT NULL DEFAULT 1 COMMENT '状态：0-禁用，1-启用',
    `create_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_user_id (`user_id`),
    INDEX idx_department_id (`department_id`),
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='医生表';

-- 4. 排班表
CREATE TABLE IF NOT EXISTS `schedule`
(
    `id`               BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `doctor_id`        BIGINT      NOT NULL COMMENT '医生ID',
    `department_id`    BIGINT      NOT NULL COMMENT '科室ID',
    `schedule_date`    DATE        NOT NULL COMMENT '排班日期',
    `time_slot`        VARCHAR(20) NOT NULL COMMENT '时间段：MORNING-上午，AFTERNOON-下午，EVENING-晚上',
    `total_number`     INT         NOT NULL COMMENT '总号源数',
    `remaining_number` INT         NOT NULL COMMENT '剩余号源数',
    `status`           INT         NOT NULL DEFAULT 1 COMMENT '状态：0-已取消，1-可预约，2-已满',
    `create_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_doctor_id (`doctor_id`),
    INDEX idx_schedule_date (`schedule_date`),
    INDEX idx_time_slot (`time_slot`),
    UNIQUE KEY uk_doctor_date_slot (`doctor_id`, `schedule_date`, `time_slot`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='排班表';

-- 5. 挂号记录表
CREATE TABLE IF NOT EXISTS `registration`
(
    `id`                BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `registration_no`   VARCHAR(50) NOT NULL UNIQUE COMMENT '挂号单号',
    `patient_id`        BIGINT      NOT NULL COMMENT '患者ID',
    `doctor_id`         BIGINT      NOT NULL COMMENT '医生ID',
    `department_id`     BIGINT      NOT NULL COMMENT '科室ID',
    `schedule_id`       BIGINT      NOT NULL COMMENT '排班ID',
    `registration_date` DATE        NOT NULL COMMENT '挂号日期（就诊日期）',
    `time_slot`         VARCHAR(20) NOT NULL COMMENT '就诊时间段',
    `queue_number`      INT COMMENT '排队号',
    `registration_fee`  DECIMAL(10, 2) COMMENT '挂号费',
    `status`            VARCHAR(20) NOT NULL COMMENT '挂号状态：PENDING-待就诊，CONSULTING-就诊中，COMPLETED-已完成，CANCELLED-已取消',
    `symptom`           VARCHAR(500) COMMENT '症状描述',
    `payment_status`    VARCHAR(20) COMMENT '支付状态：PENDING-待支付，PAID-已支付，REFUNDED-已退款',
    `payment_time`      DATETIME COMMENT '支付时间',
    `create_time`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       DATETIME    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_registration_no (`registration_no`),
    INDEX idx_patient_id (`patient_id`),
    INDEX idx_doctor_id (`doctor_id`),
    INDEX idx_registration_date (`registration_date`),
    FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`department_id`) REFERENCES `department` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`schedule_id`) REFERENCES `schedule` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='挂号记录表';

-- 6. 支付记录表
CREATE TABLE IF NOT EXISTS `payment`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `transaction_no`  VARCHAR(50)    NOT NULL UNIQUE COMMENT '交易流水号',
    `registration_id` BIGINT         NOT NULL COMMENT '挂号ID',
    `user_id`         BIGINT         NOT NULL COMMENT '用户ID',
    `amount`          DECIMAL(10, 2) NOT NULL COMMENT '支付金额',
    `payment_method`  VARCHAR(20) COMMENT '支付方式：ALIPAY-支付宝，WECHAT-微信，CASH-现金',
    `payment_status`  VARCHAR(20)    NOT NULL COMMENT '支付状态：PENDING-待支付，PAID-已支付，REFUNDED-已退款',
    `payment_time`    DATETIME COMMENT '支付时间',
    `refund_time`     DATETIME COMMENT '退款时间',
    `remark`          VARCHAR(500) COMMENT '备注',
    `create_time`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME       NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_transaction_no (`transaction_no`),
    INDEX idx_registration_id (`registration_id`),
    INDEX idx_user_id (`user_id`),
    FOREIGN KEY (`registration_id`) REFERENCES `registration` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`user_id`) REFERENCES `user` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='支付记录表';

-- 7. 就诊记录表
CREATE TABLE IF NOT EXISTS `medical_record`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `registration_id` BIGINT   NOT NULL COMMENT '挂号ID',
    `patient_id`      BIGINT   NOT NULL COMMENT '患者ID',
    `doctor_id`       BIGINT   NOT NULL COMMENT '医生ID',
    `diagnosis`       VARCHAR(1000) COMMENT '诊断结果',
    `prescription`    VARCHAR(2000) COMMENT '处方',
    `advice`          VARCHAR(1000) COMMENT '医嘱',
    `visit_time`      DATETIME COMMENT '就诊时间',
    `create_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_registration_id (`registration_id`),
    INDEX idx_patient_id (`patient_id`),
    INDEX idx_doctor_id (`doctor_id`),
    FOREIGN KEY (`registration_id`) REFERENCES `registration` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='就诊记录表';

-- 8. 医生评价表
CREATE TABLE IF NOT EXISTS `doctor_evaluation`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT '主键ID',
    `doctor_id`       BIGINT   NOT NULL COMMENT '医生ID',
    `patient_id`      BIGINT   NOT NULL COMMENT '患者ID',
    `registration_id` BIGINT   NOT NULL COMMENT '挂号ID',
    `rating`          INT      NOT NULL COMMENT '评分（1-5分）',
    `comment`         VARCHAR(1000) COMMENT '评价内容',
    `create_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    INDEX idx_doctor_id (`doctor_id`),
    INDEX idx_patient_id (`patient_id`),
    INDEX idx_registration_id (`registration_id`),
    FOREIGN KEY (`doctor_id`) REFERENCES `doctor` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`patient_id`) REFERENCES `user` (`id`) ON DELETE CASCADE,
    FOREIGN KEY (`registration_id`) REFERENCES `registration` (`id`) ON DELETE CASCADE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_unicode_ci COMMENT ='医生评价表';
