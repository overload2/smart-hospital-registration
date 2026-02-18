package com.hospital.registration.aspect;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.hospital.registration.annotation.OperationLog;
import com.hospital.registration.entity.SysOperationLog;
import com.hospital.registration.service.SysOperationLogService;
import com.hospital.registration.utils.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;

/**
 * @title 操作日志切面
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 拦截带有@OperationLog注解的方法，自动记录操作日志
 */
@Aspect
@Component
public class OperationLogAspect {

    @Autowired
    private SysOperationLogService sysOperationLogService;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    /**
     * 环绕通知，记录操作日志
     *
     * @param joinPoint 切入点
     * @param operationLog 操作日志注解
     * @return 方法执行结果
     * @throws Throwable 方法执行异常
     */
    @Around("@annotation(operationLog)")
    public Object around(ProceedingJoinPoint joinPoint, OperationLog operationLog) throws Throwable {
        // 记录开始时间
        long startTime = System.currentTimeMillis();

        // 获取请求信息
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes != null ? attributes.getRequest() : null;

        // 构建日志对象
        SysOperationLog log = new SysOperationLog();
        log.setModule(operationLog.module());
        log.setOperation(operationLog.operation());
        log.setOperationTime(LocalDateTime.now());

        // 获取请求方法
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = signature.getName();
        log.setMethod(className + "." + methodName + "()");

        // 获取请求参数
        try {
            Object[] args = joinPoint.getArgs();
            if (args != null && args.length > 0) {
                String params = objectMapper.writeValueAsString(args);
                // 限制参数长度，避免过长
                if (params.length() > 2000) {
                    params = params.substring(0, 2000) + "...";
                }
                log.setParams(params);
            }
        } catch (Exception e) {
            log.setParams("参数解析失败");
        }

        // 获取用户信息和IP
        if (request != null) {
            log.setIp(getIpAddress(request));
            // 从token中获取用户信息
            String token = request.getHeader("Authorization");

            if (token != null && !token.isEmpty()) {
                // 去掉 Bearer 前缀
                if (token.startsWith("Bearer ")) {
                    token = token.substring(7);
                }
                try {
                    Long userId = jwtUtil.getUserIdFromToken(token);
                    String username = jwtUtil.getUsernameFromToken(token);
                    log.setUserId(userId);
                    log.setUsername(username);
                } catch (Exception e) {
                    // token解析失败，忽略
                }
            }
        }

        // 执行目标方法
        Object result = null;
        try {
            result = joinPoint.proceed();
            log.setStatus(1);
        } catch (Throwable e) {
            log.setStatus(0);
            String errorMsg = e.getMessage();
            if (errorMsg != null && errorMsg.length() > 2000) {
                errorMsg = errorMsg.substring(0, 2000) + "...";
            }
            log.setErrorMsg(errorMsg);
            throw e;
        } finally {
            // 计算耗时
            long costTime = System.currentTimeMillis() - startTime;
            log.setCostTime(costTime);
            // 异步保存日志
            sysOperationLogService.recordLog(log);
        }

        return result;
    }

    /**
     * 获取客户端IP地址
     *
     * @param request HTTP请求
     * @return IP地址
     */
    private String getIpAddress(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        // 多个代理时取第一个IP
        if (ip != null && ip.contains(",")) {
            ip = ip.split(",")[0].trim();
        }
        return ip;
    }
}
