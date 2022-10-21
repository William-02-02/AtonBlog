package com.lyh.AtonBlog;

import com.lyh.AtonBlog.pojo.AdminUser;
import com.lyh.AtonBlog.service.AdminUserService;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.lyh.AtonBlog.dao")
public class DemoApplication {
    
    public static void main(String[] args) {
        SpringApplication.run(DemoApplication.class, args);
    }
    
    
}
