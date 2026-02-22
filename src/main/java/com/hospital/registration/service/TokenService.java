package com.hospital.registration.service;

/**
 * @title: TokenService
 * @author: Su
 * @date: 2026/2/21
 * @version: 1.0
 * @description: Token管理服务接口
 */
public interface TokenService {

    /**
     * 保存Token到Redis
     * @param userId 用户ID
     * @param token Token值
     * @param isAdmin 是否管理端
     */
    void saveToken(Long userId, String token, boolean isAdmin);

    /**
     * 验证Token是否有效
     * @param userId 用户ID
     * @param token Token值
     * @param isAdmin 是否管理端
     * @return true-有效，false-无效
     */
    boolean validateToken(Long userId, String token, boolean isAdmin);

    /**
     * 删除Token（登出）
     * @param userId 用户ID
     * @param isAdmin 是否管理端
     */
    void removeToken(Long userId, boolean isAdmin);
}