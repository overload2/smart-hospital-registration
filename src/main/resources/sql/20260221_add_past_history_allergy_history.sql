ALTER TABLE medical_record ADD COLUMN past_history TEXT COMMENT '既往史' AFTER present_illness;
ALTER TABLE medical_record ADD COLUMN allergy_history TEXT COMMENT '过敏史' AFTER past_history;