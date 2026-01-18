package com.hospital.registration.utils;

import com.hospital.registration.common.Constants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @title: JwtUtil
 * @author: Su
 * @date: 2026/1/12 17:27
 * @version: 1.0
 * @description: JWT工具类 - 用于生成和验证Token
 */
@Slf4j
@Component
public class JwtUtil {

    /**
     * 生成JWT Token
     * @param userId 用户ID
     * @param username 用户名
     * @return token字符串
     */
    public String generateToken(Long userId, String username) {
        // 当前时间
        Date now = new Date();
        // 过期时间
        Date expiration = new Date(now.getTime() + Constants.Jwt.EXPIRATION);

        // 创建payload（载荷）
        Map<String, Object> claims = new HashMap<>();
        claims.put("userId", userId);
        claims.put("username", username);

        // 生成密钥
        Key key = Keys.hmacShaKeyFor(Constants.Jwt.SECRET.getBytes(StandardCharsets.UTF_8));

        // 生成Token
        String token = Jwts.builder()
                .setClaims(claims)                          // 设置载荷
                .setIssuedAt(now)                          // 签发时间
                .setExpiration(expiration)                 // 过期时间
                .signWith(key, SignatureAlgorithm.HS256)   // 签名算法
                .compact();

        log.debug("生成Token成功 - userId: {}, username: {}", userId, username);
        return token;
    }

    /**
     * 解析JWT Token
     * @param token JWT字符串
     * @return Claims对象（包含用户信息）
     */
    public Claims parseToken(String token) {
        try {
            // 生成密钥
            Key key = Keys.hmacShaKeyFor(Constants.Jwt.SECRET.getBytes(StandardCharsets.UTF_8));

            // 解析Token
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            return claims;
        } catch (Exception e) {
            log.error("Token解析失败: {}", e.getMessage());
            return null;
        }
    }

    /**
     * 从Token中获取用户ID
     * @param token JWT字符串
     * @return 用户ID
     */
    public Long getUserIdFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("userId", Long.class);
        }
        return null;
    }

    /**
     * 从Token中获取用户名
     * @param token JWT字符串
     * @return 用户名
     */
    public String getUsernameFromToken(String token) {
        Claims claims = parseToken(token);
        if (claims != null) {
            return claims.get("username", String.class);
        }
        return null;
    }

    /**
     * 验证Token是否有效
     * @param token JWT字符串
     * @return true-有效，false-无效
     */
    public boolean validateToken(String token) {
        try {
            Claims claims = parseToken(token);
            if (claims == null) {
                return false;
            }

            // 检查是否过期
            Date expiration = claims.getExpiration();
            return expiration.after(new Date());
        } catch (Exception e) {
            log.error("Token验证失败: {}", e.getMessage());
            return false;
        }
    }
}

