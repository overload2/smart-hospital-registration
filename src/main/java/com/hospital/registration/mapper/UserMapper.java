package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.hospital.registration.entity.User;
import org.apache.ibatis.annotations.Mapper;

/**
* @author suzd
* @description 针对表【user(用户表)】的数据库操作Mapper
* @createDate 2026-01-18 18:55:48
* @Entity com.hospital.registration.entity.User
*/
@Mapper
public interface UserMapper extends BaseMapper<User> {

    int deleteByPrimaryKey(Long id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    // 自定义方法：根据用户名查询
    User selectByUsername( String username);
}
