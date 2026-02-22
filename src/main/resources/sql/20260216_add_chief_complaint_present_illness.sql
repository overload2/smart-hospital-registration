ALTER TABLE medical_record
    ADD COLUMN chief_complaint VARCHAR(500) DEFAULT NULL COMMENT '主诉' AFTER doctor_id;

ALTER TABLE medical_record
    ADD COLUMN present_illness TEXT DEFAULT NULL COMMENT '现病史' AFTER chief_complaint;

