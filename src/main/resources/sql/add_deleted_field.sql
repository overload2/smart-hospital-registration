-- 为所有表添加逻辑删除字段

-- 1. 用户表
ALTER TABLE user ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_user_deleted ON user(deleted);

-- 2. 科室表
ALTER TABLE department ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_department_deleted ON department(deleted);

-- 3. 医生表
ALTER TABLE doctor ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_doctor_deleted ON doctor(deleted);

-- 4. 排班表
ALTER TABLE schedule ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_schedule_deleted ON schedule(deleted);

-- 5. 挂号表
ALTER TABLE registration ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_registration_deleted ON registration(deleted);

-- 6. 支付表
ALTER TABLE payment ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_payment_deleted ON payment(deleted);

-- 7. 病历表
ALTER TABLE medical_record ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_medical_record_deleted ON medical_record(deleted);

-- 8. 医生评价表
ALTER TABLE doctor_evaluation ADD COLUMN deleted TINYINT DEFAULT 0 COMMENT '逻辑删除标识：0-未删除，1-已删除';
CREATE INDEX idx_doctor_evaluation_deleted ON doctor_evaluation(deleted);