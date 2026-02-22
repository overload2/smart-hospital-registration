package com.hospital.registration;

import lombok.extern.slf4j.Slf4j;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

@Slf4j
@SpringBootApplication
@MapperScan("com.hospital.registration.mapper")
@EnableAsync
public class HospitalRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalRegistrationApplication.class, args);
        log.info("Hospital Registration Application started successfully 项目初始化成功.");
    }

}
