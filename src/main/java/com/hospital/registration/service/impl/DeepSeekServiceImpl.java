package com.hospital.registration.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.registration.config.DeepSeekConfig;
import com.hospital.registration.service.DeepSeekService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * @title DeepSeek服务实现类
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description DeepSeek AI调用实现
 */
@Slf4j
@Service
public class DeepSeekServiceImpl implements DeepSeekService {

    @Autowired
    private DeepSeekConfig deepSeekConfig;

    @Autowired
    private ObjectMapper objectMapper;

    private final OkHttpClient httpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(60, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build();

    /**
     * 发送对话请求
     *
     * @param messages 消息列表
     * @return AI回复内容
     */
    @Override
    public String chat(List<Map<String, String>> messages) {
        try {
            // 构建请求体
            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("model", deepSeekConfig.getModel());
            requestBody.put("messages", messages);
            requestBody.put("max_tokens", deepSeekConfig.getMaxTokens());
            requestBody.put("temperature", deepSeekConfig.getTemperature());

            String jsonBody = objectMapper.writeValueAsString(requestBody);
            log.info("DeepSeek请求: {}", jsonBody);

            // 发送请求
            Request request = new Request.Builder()
                    .url(deepSeekConfig.getBaseUrl() + "/chat/completions")
                    .addHeader("Authorization", "Bearer " + deepSeekConfig.getApiKey())
                    .addHeader("Content-Type", "application/json")
                    .post(RequestBody.create(jsonBody, MediaType.parse("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    log.error("DeepSeek请求失败: {}", response.code());
                    throw new RuntimeException("AI服务请求失败");
                }

                String responseBody = response.body().string();
                log.info("DeepSeek响应: {}", responseBody);

                // 解析响应
                JsonNode jsonNode = objectMapper.readTree(responseBody);
                return jsonNode.path("choices").path(0).path("message").path("content").asText();
            }
        } catch (Exception e) {
            log.error("DeepSeek调用异常: {}", e.getMessage(), e);
            throw new RuntimeException("AI服务调用失败: " + e.getMessage());
        }
    }
}
