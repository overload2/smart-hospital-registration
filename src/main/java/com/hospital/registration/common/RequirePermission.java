package com.hospital.registration.common;

import java.lang.annotation.*;

/**
 * @title: RequirePermission
 * @author: Su
 * @date: 2026/2/17
 * @version: 1.0
 * @description: 权限校验注解，标注在Controller方法上，用于接口权限控制
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequirePermission {

    /**
     * 需要的权限编码
     * 例如：@RequirePermission("system:user:add")
     */
    String value();
}
