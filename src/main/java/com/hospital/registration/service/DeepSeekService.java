package com.hospital.registration.service;

import java.util.List;
import java.util.Map;

/**
 * @title DeepSeek服务接口
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description DeepSeek AI调用接口
 */
public interface DeepSeekService {

    /**
     * 发送对话请求
     *
     * @param messages 消息列表，格式：[{"role": "user/assistant/system", "content": "内容"}]
     * @return AI回复内容
     */
    String chat(List<Map<String, String>> messages);
}
