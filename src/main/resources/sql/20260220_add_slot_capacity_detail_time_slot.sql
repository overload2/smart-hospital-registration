-- 排班表增加字段：
-- 每个细分时段的号源数
ALTER TABLE schedule ADD COLUMN slot_capacity INT DEFAULT 5 COMMENT '每个细分时段号源数';

-- 挂号表增加字段
-- 患者选择的具体时段
ALTER TABLE registration ADD COLUMN detail_time_slot VARCHAR(20) COMMENT '细分时段，如 M_0800';