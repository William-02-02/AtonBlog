package com.lyh.AtonBlog.config;

import com.lyh.AtonBlog.controller.common.OnlineUserStatisticsService;
import com.lyh.AtonBlog.interceptor.LoginInterceptor;
import com.lyh.AtonBlog.interceptor.OnlineUserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AtonBlogWebMvcConfig implements WebMvcConfigurer {
    
    @Autowired
    LoginInterceptor loginInterceptor;
    
    @Autowired
    OnlineUserInterceptor onlineUserInterceptor;
    
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/admin/login")
                .excludePathPatterns("/admin/dist/**")
                .excludePathPatterns("/admin/plugins/**");
        
        registry.addInterceptor(onlineUserInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/static")
                .excludePathPatterns("/admin/**");
        
    }
    
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**").
                addResourceLocations("file:"+Constants.FILE_UPLOAD_DIC);
        
    }
}
