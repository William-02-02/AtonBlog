package com.lyh.AtonBlog.dao;

import com.lyh.AtonBlog.pojo.AdminUser;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminUserMapper {
    
    /**
     * 插入一个用户(全属性)
     */
    int addUser(AdminUser user);
    
    /**
     * 插入一个用户（属性可选）
     */
    int addUserSelective(AdminUser user);
    
    /**
     * 登陆
     */
    AdminUser login(@Param("userName") String account, @Param("password") String password);
    
    /**
     * 查找所有用户
     */
    List<AdminUser> selectAllUser();
    
    /**
     * 根据id搜索用户对象
     */
    AdminUser selectByPrimaryKey(Integer adminUserId);
    
    /**
     * 根据用户名搜索用户对象
     */
    AdminUser selectByLoginUserName(@Param("loginUserName") String loginUserName);
    
    
    /**
     * 根据id更新用户数据
     */
    int updateByPrimaryKey(AdminUser adminUser);
    
    /**
     * 选择性更新用户信息
     */
    int updateByPrimaryKeySelective(AdminUser adminUser);
    
    /**
     * delete
     */
    int deleteById(Integer adminUserId);
    
    
}
