-- ====================================
-- 智能医院挂号系统 - 初始化数据
-- ====================================

-- 1. 插入测试用户（密码都是123456，BCrypt加密后的值）
INSERT INTO `user` (`username`, `password`, `real_name`, `phone`, `gender`, `role`, `status`)
VALUES ('admin', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '管理员', '13800000000', 'MALE',
        'ADMIN', 1),
       ('doctor_wang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '王医生', '13800000001', 'MALE',
        'DOCTOR', 1),
       ('doctor_li', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '李医生', '13800000002', 'FEMALE',
        'DOCTOR', 1),
       ('patient_zhang', '$2a$10$N.zmdr9k7uOCQb376NoUnuTJ8iAt6Z5EHsM8lE9lBOsl7iKTVKIUi', '张患者', '13900000001',
        'MALE', 'PATIENT', 1);

-- 2. 插入科室数据
INSERT INTO `department` (`name`, `code`, `description`, `location`, `phone`)
VALUES ('内科', 'NEIKE', '内科诊疗服务', '1号楼2楼', '0571-12345601'),
       ('外科', 'WAIKE', '外科诊疗服务', '1号楼3楼', '0571-12345602'),
       ('儿科', 'ERKE', '儿科诊疗服务', '2号楼1楼', '0571-12345603'),
       ('妇产科', 'FUCHANKEKE', '妇产科诊疗服务', '2号楼2楼', '0571-12345604');

-- 3. 插入医生数据
INSERT INTO `doctor` (`user_id`, `department_id`, `title`, `specialty`, `introduction`, `registration_fee`, `rating`)
VALUES (2, 1, '主任医师', '心血管疾病、高血压', '从事内科临床工作20年，擅长心血管疾病诊治', 50.00, 4.8),
       (3, 3, '副主任医师', '儿童呼吸道疾病、过敏性疾病', '从事儿科临床工作15年，对儿童常见病有丰富经验', 30.00, 4.5);

