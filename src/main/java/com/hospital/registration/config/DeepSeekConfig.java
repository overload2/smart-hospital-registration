package com.hospital.registration.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * @title DeepSeek配置类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description DeepSeek AI接口配置
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "deepseek")
public class DeepSeekConfig {

    // API密钥
    private String apiKey;

    // 接口地址
    private String baseUrl;

    // 模型名称
    private String model;

    // 最大token数
    private Integer maxTokens;

    // 温度参数(0-1，越高越随机)
    private Double temperature;
}
