package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.AdminUser;
import org.apache.ibatis.annotations.Param;

public interface AdminUserService {
    
    
    
    /**
     * 插入一个用户(全属性)
     */
     Boolean addUser(AdminUser user);
    
    /**
     * 插入一个用户（属性可选）
     */
    Boolean addUserSelective(AdminUser user);
    
    /**
     * 登陆
     */
    AdminUser login(String userName, String password);
    
    /**
     * 根据id搜索用户对象
     */
    AdminUser getUserInfoById(Integer userId);
    
    /**
     * 更新用户密码
     */
    Boolean updatePassword(Integer userId,String originalPassword, String newPassword);
    
    /**
     * 更新用户名字
     */
    Boolean updateName(Integer userId,String loginUserName, String nickName);
    
    /**
     * 根据id更新用户数据
     */
    Boolean updataByPrimaryKey(AdminUser adminUser);
    
    /**
     * 选择性更新用户信息
     */
    Boolean updateByPrimaryKeySelective(AdminUser adminUser);
    
    /**
     * delete
     */
    Boolean deleteById(Integer adminUserId);
    
}
