package com.hospital.registration.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.hospital.registration.entity.User;
import com.hospital.registration.vo.UserVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

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

    /**
     * 根据角色编码查询用户列表
     * @param roleCode 角色编码
     * @return 用户VO列表
     */
    List<UserVO> selectUsersByRoleCode(String roleCode);

    /**
     * 查询所有启用的用户
     */
    List<User> selectActiveUsers();

    /**
     * 分页条件查询用户
     */
    Page<UserVO> selectPageByCondition(Page<UserVO> page,
                                       @Param("username") String username,
                                       @Param("realName") String realName,
                                       @Param("phone") String phone,
                                       @Param("status") Integer status);

    /**
     * 根据ID查询用户详情（包含角色）
     */
    UserVO selectUserDetailById(@Param("id") Long id);

    /**
     * 批量更新用户状态
     */
    int batchUpdateStatus(@Param("ids") List<Long> ids, @Param("status") Integer status);

    /**
     * 重置密码
     */
    int updatePassword(@Param("id") Long id, @Param("password") String password);

    /**
     * 根据手机号查询用户
     */
    User selectByPhone(@Param("phone") String phone);
}
