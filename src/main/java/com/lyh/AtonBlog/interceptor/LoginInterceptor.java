package com.lyh.AtonBlog.interceptor;

import com.lyh.AtonBlog.controller.common.OnlineUserStatisticsService;
import com.lyh.AtonBlog.dao.AdminUserMapper;
import com.lyh.AtonBlog.pojo.AdminUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.Duration;

@Component
public class LoginInterceptor implements HandlerInterceptor {
    
    @Autowired
    AdminUserMapper adminUserMapper;
    
    
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        
        //从request中获取cookie 判空
        Cookie[] cookie = request.getCookies();
        HttpSession session = request.getSession();

        //因为有前台页面不需要登陆也可以访问 所以这里要获取访问的路径 判断是不是后台
        String servletPath = request.getServletPath();
        if (!servletPath.startsWith("/admin")){
           
            return true;
        }
        
        //判断有无cookie 并获取cookie内容即userName
        String userName = "";
        boolean haveCookie = false;
        if (cookie != null){
            for (Cookie temp : cookie) {
                if (temp.getName().equals("cookie_userName") && temp.getValue() !=null) {
                    userName = temp.getValue();
                    haveCookie = true;
                    break;    
                }
            }
        }
        
        //判断有没有session 1.不为空 2.要有loginUser 和loginId
        boolean haveSession =false;
        if (session != null && 
                session.getAttribute("loginUser") != null && 
                session.getAttribute("loginUserId") != null){
            haveSession = true;
        }
        
        //1.记住密码：有cookie 判断有没有session 有就直接进入 没有的话就补充session 然后进入
        //2.不记住密码：无cookie 判断有无session 有的话就进入 没有就退出
        //3.保证访问的页面是以/admin开头
        if (haveCookie){
            //存在cookie 检测session是否存在 没有就补充
            if (!haveSession){
                //session过期 而cookie还有
                AdminUser adminUser = adminUserMapper.selectByLoginUserName(userName);
                session.setAttribute("loginUser",adminUser.getNickName());
                session.setAttribute("loginUserId",adminUser.getAdminUserId());
                session.setMaxInactiveInterval(60*60*12);
            }
            return true;
        }else {
            //没有cookie 判断有没有session
            if (haveSession){
                return true;
            }else {
                //测试重定向能否使用request中的内容 结果是不能
                // request.setAttribute("errorMsg","用户信息已过期，请重写登陆");
                request.getSession().setAttribute("errorMsg","登陆信息已过期，请重新登陆");
                response.sendRedirect("/admin/login");
                return false;
            }
        }
       
    
        /**
         * 原项目 用session存储 无记住密码
         */
        // String servletPath = request.getServletPath();
        // if (request.getSession().getAttribute("loginUser") == null && servletPath.startsWith("/admin")){
        //     request.getSession().setAttribute("errorMsg","登陆超时，请重新登陆");
        //     response.sendRedirect("/admin/login");
        //     return false;
        // }else {
        //     return true;
        // }
    
        /**
         * 下方例子不使用我的项目，不知道为什么 idea会自动生成两个cookie 所以这个判空就太简易了
         */
        //
        // //这里存在两种情况 一种是记住了密码有cookie 还有一种是不勾选记住密码只有session
        // if (cookie == null && servletPath.startsWith("/admin") && session == null){
        //     //这里测试以下request中存值能不能在重定向后读取
        //     request.setAttribute("errorMsg","用户信息过期，请重新登陆");
        //     response.sendRedirect("/admin/login");
        //     return false;
        // }else if (cookie == null && servletPath.startsWith("/admin") && session != null){
        //     if (request.getSession().getAttribute("loginUser") == null && servletPath.startsWith("/admin")){
        //         request.getSession().setAttribute("errorMsg","登陆超时，请重新登陆");
        //         response.sendRedirect("/admin/login");
        //         return false;
        //     }else {
        //         return true;
        //     }
        // }
        //
        // //遍历cookie
        // Cookie[] cookies = request.getCookies();
        // //存放cookie中读取的内容
        // String userName = "";
        // for (Cookie item : cookies) {
        //     if (item.getName().equals("cookie_userName")){
        //         userName = item.getValue();
        //         break;
        //     }
        // }
        //
        // //检查cookie中的内容是不是空
        // if (userName.isEmpty()){
        //     response.sendRedirect("/admin/login");
        //     return false;
        // }
        //
        // //session如果过期了 而cookie还在（session在服务器端，cookie在本地）
        // Object loginUser = session.getAttribute("loginUser");
        // if (loginUser == null){
        //     AdminUser adminUser = adminUserMapper.selectByLoginUserName(userName);
        //     session.setAttribute("loginUser",adminUser.getNickName());
        //     session.setAttribute("loginUserId",adminUser.getAdminUserId());
        //     session.setMaxInactiveInterval(60*60*12);
        // }
    }
    
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        
    }
    
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        
    }
}
