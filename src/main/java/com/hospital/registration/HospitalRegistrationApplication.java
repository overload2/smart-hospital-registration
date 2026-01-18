package com.hospital.registration;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.hospital.registration.mapper")
public class HospitalRegistrationApplication {

    public static void main(String[] args) {
        SpringApplication.run(HospitalRegistrationApplication.class, args);
    }

}
