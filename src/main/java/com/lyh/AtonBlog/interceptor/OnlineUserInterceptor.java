package com.lyh.AtonBlog.interceptor;

import com.lyh.AtonBlog.controller.common.OnlineUserStatisticsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Duration;

@Component
public class OnlineUserInterceptor implements HandlerInterceptor {
    
    @Autowired
    OnlineUserStatisticsService onlineUserService;
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String IP = request.getRemoteAddr();
        onlineUserService.clear(Duration.ofDays(1));
        onlineUserService.online(IP);
        return true;
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
    }
}
