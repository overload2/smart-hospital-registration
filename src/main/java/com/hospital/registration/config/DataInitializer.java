package com.hospital.registration.config;

import com.hospital.registration.common.Gender;
import com.hospital.registration.common.UserRole;
import com.hospital.registration.entity.Department;
import com.hospital.registration.entity.User;
import com.hospital.registration.mapper.DepartmentRepository;
import com.hospital.registration.mapper.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

/**
 * @title: DataInitializer
 * @author: Su
 * @date: 2026/1/11 20:05
 * @version: 1.0
 * @description: 数据初始化类 - 应用启动时自动执行
 */
@Slf4j
@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    public DataInitializer(UserRepository userRepository,
                           DepartmentRepository departmentRepository) {
        this.userRepository = userRepository;
        this.departmentRepository = departmentRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        log.info("=== 开始数据初始化 ===");

        // 初始化管理员账号
        initAdminUser();

        // 初始化科室数据
        initDepartments();

        log.info("=== 数据初始化完成 ===");
    }

    /**
     * 初始化管理员账号
     */
    private void initAdminUser() {
        // 检查管理员是否已存在
        User existAdmin = userRepository.findByUsername("admin");
        if (existAdmin != null) {
            log.info("管理员账号已存在，跳过初始化");
            return;
        }

        // 创建管理员账号
        User admin = new User();
        admin.setUsername("admin");
        admin.setPassword("admin123"); // 注意：实际项目应该加密
        admin.setRealName("系统管理员");
        admin.setPhone("13800138000");
        admin.setGender(Gender.MALE);
        admin.setRole(UserRole.ADMIN);
        admin.setStatus(1);

        userRepository.save(admin);
        log.info("管理员账号初始化成功 - 用户名: admin, 密码: admin123");
    }

    /**
     * 初始化科室数据
     */
    private void initDepartments() {
        String[] deptCodes = {"NEI", "WAI", "ER", "FU", "YAN", "KOU"};
        String[] deptNames = {"内科", "外科", "儿科", "妇产科", "眼科", "口腔科"};
        String[] locations = {"1号楼2层", "1号楼3层", "2号楼1层", "2号楼2层", "3号楼1层", "3号楼2层"};

        for (int i = 0; i < deptCodes.length; i++) {
            // 检查科室是否已存在
            Department existDept = departmentRepository.findByCode(deptCodes[i]);
            if (existDept != null) {
                continue;
            }

            // 创建科室
            Department dept = new Department();
            dept.setCode(deptCodes[i]);
            dept.setName(deptNames[i]);
            dept.setDescription(deptNames[i] + "诊疗服务");
            dept.setLocation(locations[i]);
            dept.setPhone("0571-8888" + (1000 + i));
            dept.setStatus(1);

            departmentRepository.save(dept);
            log.info("科室初始化成功 - {}", deptNames[i]);
        }
    }
}
