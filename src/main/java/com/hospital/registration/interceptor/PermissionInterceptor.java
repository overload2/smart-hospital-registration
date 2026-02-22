package com.hospital.registration.interceptor;

import com.hospital.registration.common.BusinessException;
import com.hospital.registration.common.Constants;
import com.hospital.registration.common.RequirePermission;
import com.hospital.registration.common.ResultCode;
import com.hospital.registration.service.PermissionService;
import com.hospital.registration.service.TokenService;
import com.hospital.registration.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

/**
 * @title: PermissionInterceptor
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限拦截器，校验用户是否拥有接口所需权限
 */
@Slf4j
@Component
public class PermissionInterceptor implements HandlerInterceptor {

    private final JwtUtil jwtUtil;
    private final PermissionService permissionService;
    private final TokenService tokenService;

    /**
     * 构造器注入
     */
    public PermissionInterceptor(JwtUtil jwtUtil, PermissionService permissionService,TokenService tokenService) {
        this.jwtUtil = jwtUtil;
        this.permissionService = permissionService;
        this.tokenService = tokenService;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // 放行 OPTIONS 预检请求
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            return true;
        }

        // 如果不是方法处理器，直接放行
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        // 从请求头获取Token
        String token = request.getHeader("Authorization");
        if (token == null || token.isEmpty()) {
            log.warn("权限校验失败 - 未提供Token");
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
            log.warn("权限校验失败 - Token无效: {}", e.getMessage());
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }
        if (userId == null) {
            log.warn("权限校验失败 - 无法获取用户ID");
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }
        // 验证Token是否在Redis中有效（未登出）
        if (!tokenService.validateToken(userId, token, true)) {
            log.warn("权限校验失败 - Token已失效或已登出, 用户ID: {}", userId);
            throw new BusinessException(ResultCode.UNAUTHORIZED.getCode(), "登录已过期，请重新登录");
        }

        // 将用户ID存入request，供Controller使用
        request.setAttribute("userId", userId);
        // 获取方法上的权限注解
        RequirePermission requirePermission = handlerMethod.getMethodAnnotation(RequirePermission.class);

        // 如果没有权限注解，直接放行
        if (requirePermission == null) {
            return true;
        }

        // 获取需要的权限编码
        String permissionCode = requirePermission.value();
        log.info("权限校验 - 接口: {}, 需要权限: {}", request.getRequestURI(), permissionCode);

        // 超级管理员跳过权限检查
        if (permissionService.isSuperAdmin(userId)) {
            log.info("超级管理员跳过权限检查 - 用户ID: {}", userId);
            return true;
        }
        // 校验用户是否拥有该权限
        boolean hasPermission = permissionService.hasPermission(userId, permissionCode);
        if (!hasPermission) {
            log.warn("权限校验失败 - 用户ID: {}, 缺少权限: {}", userId, permissionCode);
            throw new BusinessException(ResultCode.FORBIDDEN.getCode(), "没有操作权限");
        }

        log.info("权限校验通过 - 用户ID: {}, 权限: {}", userId, permissionCode);
        return true;
    }
}
