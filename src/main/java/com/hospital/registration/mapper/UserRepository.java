package com.hospital.registration.mapper;

import com.hospital.registration.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @title: UserRepository
 * @author: Su
 * @date: 2026/1/11 20:00
 * @version: 1.0
 * @description: 用户数据访问接口
 */
@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    // 根据用户名查询
    User findByUsername(String username);
}
