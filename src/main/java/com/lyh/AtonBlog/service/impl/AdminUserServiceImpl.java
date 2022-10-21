package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.AdminUserMapper;
import com.lyh.AtonBlog.pojo.AdminUser;
import com.lyh.AtonBlog.service.AdminUserService;
import com.lyh.AtonBlog.util.MD5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class AdminUserServiceImpl implements AdminUserService {
    
    @Autowired
    private AdminUserMapper adminUserMapper;
    
    @Override
    public Boolean addUser(AdminUser user) {
    
        //非空才允许添加用户
        int i = 0;
        if (user != null){
            //加密密码
            String s = MD5Util.MD5Encode(user.getLoginPassword(), "UTF-8");
            user.setLoginPassword(s);
            i = adminUserMapper.addUser(user);
        }
        
        return i == 1? true:false;
    }
    
    @Override
    public Boolean addUserSelective(AdminUser user) {
        
        //非空才允许添加用户
        int i = 0;
        if (user != null){
            //加密密码
            String s = MD5Util.MD5Encode(user.getLoginPassword(), "UTF-8");
            user.setLoginPassword(s);
            i = adminUserMapper.addUserSelective(user);
        }
        return i == 1? true:false;
    }
    
    @Override
    public AdminUser login(String userName, String password) {
        
        String passwordMd5 = MD5Util.MD5Encode(password,"UTF-8");
        AdminUser user = adminUserMapper.login(userName, passwordMd5);
        if (user == null){
            System.out.println("AdminUserServiceImpl.login.user：为空");
        }
        
        
        return user;
    }
    
    @Override
    public AdminUser getUserInfoById(Integer userId) {
        return adminUserMapper.selectByPrimaryKey(userId);
    }
    
    @Override
    public Boolean updatePassword(Integer userId, String originalPassword, String newPassword) {
        //先通过id查到需要更新的用户
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(userId);
        //查到了 再继续 空的话就fuck off
        if (adminUser != null){
            //将两个密码进行加密 比对旧密码
            String orgPassMD5 = MD5Util.MD5Encode(originalPassword, "UTF-8");
            String newPassMD5 = MD5Util.MD5Encode(newPassword, "UTF-8");
            
            if (orgPassMD5.equals(adminUser.getLoginPassword())){
                //设置新密码 并修改
                adminUser.setLoginPassword(newPassMD5);
                if (adminUserMapper.updateByPrimaryKeySelective(adminUser) > 0){
                    return true;
                }
            }
        }
        return false;
    }
    
    @Override
    public Boolean updateName(Integer userId, String loginUserName, String nickName) {
        //先通过id查到需要更新的用户
        AdminUser adminUser = adminUserMapper.selectByPrimaryKey(userId);
        //查到了 再继续 空的话就fuck off
        if (adminUser != null){
            //直接将需要修改的信息封装到对象中
            adminUser.setLoginUserName(loginUserName);
            adminUser.setNickName(nickName);
    
            int i = adminUserMapper.updateByPrimaryKeySelective(adminUser);
            System.out.println("AdminUserServiceImpl.updateName.更新条数i:" + i);
            if (i > 0){
                System.out.println("AdminUserServiceImpl：成功更新");
                //修改成功
                return true;
            }
        }
    
        System.out.println("AdminUserServiceImpl：更新失败");
        return false;
    }
    
    @Override
    public Boolean deleteById(Integer adminUserId) {
        return null;
    }
    
    
    
    //备用 以供扩展
    @Override
    public Boolean updataByPrimaryKey(AdminUser adminUser) {
        return null;
    }
    
    @Override
    public Boolean updateByPrimaryKeySelective(AdminUser adminUser) {
        return null;
    }
    
   
}
