package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.AdminUser;
import org.junit.jupiter.api.Test;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class AdminUserTest {
    
    @Autowired
    AdminUserService adminUserServiceImpl;
    
    @Test
    public void userTest(){
        
        AdminUser testUser = new AdminUser();
        testUser.setLoginUserName("Aton");
        testUser.setLoginPassword("123456");
        testUser.setNickName("Williams");
        
        adminUserServiceImpl.addUser(testUser);
    }
    
}
