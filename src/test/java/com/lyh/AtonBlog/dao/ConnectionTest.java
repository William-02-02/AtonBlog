package com.lyh.AtonBlog.dao;

import com.lyh.AtonBlog.pojo.AdminUser;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import javax.sql.DataSource;

@SpringBootTest
public class ConnectionTest {
    @Autowired
    DataSource dataSource;

    @Test
    public void getDataSource(){
        System.out.println(dataSource);
    }
    
    @Autowired
    AdminUserMapper adminUserMapper;
    
    @Test
    public void daoTest(){
        adminUserMapper.addUser(new AdminUser(1,"aa","bbb","an", (byte) 1));
    }
    
}
