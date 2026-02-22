package com.hospital.registration.config;

import com.hospital.registration.interceptor.AppAuthInterceptor;
import com.hospital.registration.interceptor.PermissionInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @title: WebMvcConfig
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: Web MVC配置类
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    private final PermissionInterceptor permissionInterceptor;
    private final AppAuthInterceptor appAuthInterceptor;

    /**
     * 构造器注入
     */
    public WebMvcConfig(PermissionInterceptor permissionInterceptor, AppAuthInterceptor appAuthInterceptor) {
        this.permissionInterceptor = permissionInterceptor;
        this.appAuthInterceptor = appAuthInterceptor;
    }

    /**
     * 添加拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        //管理端拦截器
        registry.addInterceptor(permissionInterceptor)
                .addPathPatterns("/**")  // 拦截所有请求
                .excludePathPatterns(    // 排除不需要权限校验的接口
                        "/user/login",
                        "/user/register",
                        "/error",
                        "/dashboard/stats", //首页统计不需要登陆
                        "/app/**" //排除患者端，由AppAuthInterceptor处理
                );
        // 患者端拦截器
        registry.addInterceptor(appAuthInterceptor)
                .addPathPatterns("/app/**")
                .excludePathPatterns(
                        "/app/auth/login",
                        "/app/auth/register",
                        "/app/auth/wx-login",
                        "/app/department/**",  // 科室列表无需登录
                        "/app/doctor/**",       // 医生列表无需登录
                        "/app/schedule/**",      // 排班列表无需登录
                        "/app/announcement/**"  // 公告列表无需登录
                );

    }

    // 跨域配置
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("*")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .allowCredentials(true)
                .maxAge(3600);
    }
}
