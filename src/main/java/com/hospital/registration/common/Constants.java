package com.hospital.registration.common;

/**
 * @title: Constants
 * @author: Su
 * @date: 2026/1/11 17:24
 * @version: 1.0
 * @description: 系统常量类
 */

public final class Constants {
    // 私有构造方法，防止实例化
    private Constants() {
    }

    /**
     * 挂号相关常量
     */
    public static final class Registration {
        // 每日每个医生最大挂号数
        public static final Integer MAX_PER_DAY = 50;
        // 可提前预约的天数
        public static final Integer ADVANCE_DAYS = 7;
        // 取消挂号的最晚时间（小时）
        public static final Integer CANCEL_BEFORE_HOURS = 2;
    }

    /**
     * 用户相关常量
     */
    public static final class User {
        // 默认密码
        public static final String DEFAULT_PASSWORD = "123456";
        // 密码最小长度
        public static final Integer PASSWORD_MIN_LENGTH = 6;
        // 密码最大长度
        public static final Integer PASSWORD_MAX_LENGTH = 20;
    }

    /**
     * JWT相关常量
     */
    public static final class Jwt {
        // Token过期时间（毫秒）7天
        public static final Long EXPIRATION = 1 * 60 * 60 * 1000L;
        // Token请求头名称
        public static final String HEADER = "Authorization";
        // Token前缀
        public static final String PREFIX = "Bearer ";
        // 密钥（实际项目应该放在配置文件中）
        public static final String SECRET = "hospital_registration_secret_key_2025";
    }

    /**
     * 日期时间格式常量
     */
    public static final class DateTime {
        public static final String DATE_FORMAT = "yyyy-MM-dd";
        public static final String TIME_FORMAT = "HH:mm:ss";
        public static final String DATETIME_FORMAT = "yyyy-MM-dd HH:mm:ss";
    }

    /**
     * 支付相关常量
     */
    public static final class Payment {
        // 支付超时时间（分钟）
        public static final Integer TIMEOUT_MINUTES = 30;
        // 退款处理天数
        public static final Integer REFUND_DAYS = 3;
    }

}
