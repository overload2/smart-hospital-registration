package com.hospital.registration.annotation;

import java.lang.annotation.*;

/**
 * @title 操作日志注解
 * @author developer
 * @date 2026-02-18
 * @version 1.0
 * @description 标注在Controller方法上，自动记录操作日志
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface OperationLog {

    // 操作模块
    String module();

    // 操作类型(ADD/UPDATE/DELETE/QUERY/OTHER)
    String operation();
}
