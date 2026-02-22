package com.hospital.registration.service.impl;

import com.hospital.registration.common.Constants;
import com.hospital.registration.service.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

/**
 * @title: TokenServiceImpl
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: Token管理服务实现类
 */
@Slf4j
@Service
public class TokenServiceImpl implements TokenService {

    private final StringRedisTemplate redisTemplate;

    private static final String TOKEN_PREFIX_ADMIN = "token:admin:";
    private static final String TOKEN_PREFIX_APP = "token:app:";

    public TokenServiceImpl(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 保存Token到Redis
     * @param userId 用户ID
     * @param token 令牌
     * @param isAdmin 是否为管理员
     */
    @Override
    public void saveToken(Long userId, String token, boolean isAdmin) {
        String key = getKey(userId, isAdmin);
        long expireTime = Constants.Jwt.EXPIRATION;
        redisTemplate.opsForValue().set(key, token, expireTime, TimeUnit.MILLISECONDS);
        log.info("Token已保存 - userId: {}, isAdmin: {}", userId, isAdmin);
    }

    /**
     * 验证Token
     * @param userId 用户ID
     * @param token 令牌
     * @param isAdmin 是否为管理员
     * @return 验证结果
     */
    @Override
    public boolean validateToken(Long userId, String token, boolean isAdmin) {
        String key = getKey(userId, isAdmin);
        String storedToken = redisTemplate.opsForValue().get(key);
        return token != null && token.equals(storedToken);
    }
    /**
     * 删除Token
     * @param userId 用户ID
     * @param isAdmin 是否管理端
     */
    @Override
    public void removeToken(Long userId, boolean isAdmin) {
        String key = getKey(userId, isAdmin);
        redisTemplate.delete(key);
        log.info("Token已删除 - userId: {}, isAdmin: {}", userId, isAdmin);
    }

    /**
     * 获取Token的Key
     * @param userId 用户ID
     * @param isAdmin 是否管理端
     * @return Token的Key
     */
    private String getKey(Long userId, boolean isAdmin) {
        String prefix = isAdmin ? TOKEN_PREFIX_ADMIN : TOKEN_PREFIX_APP;
        return prefix + userId;
    }
}
