package com.hospital.registration.interceptor;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.Constants;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.service.TokenService;
import com.hospital.registration.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @title: AppAuthInterceptor
 * @author: Su
 * @date: 2026/2/20
 * @version: 1.0
 * @description: 患者端认证拦截器
 */
@Slf4j
@Component
public class AppAuthInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final TokenService tokenService;

    public AppAuthInterceptor(JwtUtil jwtUtil, TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }


        // 从请求头获取Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            log.warn("患者端认证失败 - 未提供Token, URI: {}", request.getRequestURI());
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "请先登录");
        }

        // 去掉Bearer前缀
        if (token.startsWith(Constants.Jwt.PREFIX)) {
            token = token.substring(7);
        }

        // 验证Token并获取用户ID
        Long userId;
        try {
            userId = jwtUtil.getUserIdFromToken(token);
        } catch (Exception e) {
            log.warn("患者端认证失败 - Token无效: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }

        if (userId == null) {
            log.warn("患者端认证失败 - 无法获取用户ID");
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }

        //验证Token是否在Redis中有效（未登出）
        if (!tokenService.validateToken(userId, token, false)) {
            log.warn("患者端认证失败 - Token已失效或已登出, 用户ID: {}", userId);
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }

        // 将用户ID存入request，供Controller使用
        request.setAttribute("userId", userId);
        log.debug("患者端认证通过 - 用户ID: {}", userId);
        return true;
    }
}
