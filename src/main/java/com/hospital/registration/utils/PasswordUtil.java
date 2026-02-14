package com.hospital.registration.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @title: PasswordUtil
 * @author: Su
 * @date: 2026/1/12 17:54
 * @version: 1.0
 * @description: 密码加密工具类 - 使用BCrypt加密算法
 */
@Slf4j
@Component
public class PasswordUtil {

    // BCrypt加密器
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    /**
     * 加密密码
     * @param rawPassword 原始密码（明文）
     * @return 加密后的密码（密文）
     */
    public String encode(String rawPassword) {
        String encodedPassword = encoder.encode(rawPassword);
        log.debug("密码加密成功");
        return encodedPassword;
    }

    /**
     * 验证密码
     * @param rawPassword 原始密码（明文）
     * @param encodedPassword 加密后的密码（密文）
     * @return true-密码正确，false-密码错误
     */
    public boolean matches(String rawPassword, String encodedPassword) {
        boolean result = encoder.matches(rawPassword, encodedPassword);
        log.debug("密码验证结果: {}", result);
        return result;
    }
}

