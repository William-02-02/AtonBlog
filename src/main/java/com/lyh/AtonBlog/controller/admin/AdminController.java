package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.controller.common.CommonController;
import com.lyh.AtonBlog.controller.common.OnlineUserStatisticsService;
import com.lyh.AtonBlog.pojo.AdminUser;
import com.lyh.AtonBlog.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@Controller
@RequestMapping("/admin")
@Api(tags = "用户管理")
public class AdminController {
    
    @Autowired
    AdminUserService adminUserService;
    @Autowired
    BlogService blogService;
    @Autowired
    CategoryService categoryService;
    @Autowired
    TagService tagService;
    @Autowired
    LinkService linkService;
    @Autowired
    CommentService commentService;
    
    @Autowired
    OnlineUserStatisticsService onlineUserStatsService;
    
    
    @GetMapping("/login")
    @ApiOperation("跳转登陆页面")
    public String login(){
        return "admin/login";
    }
    
    
    @PostMapping("/login")
    @ApiOperation("用户登陆")
    public String login(@RequestParam("userName")String userName,
                        @RequestParam("password")String password,
                        @RequestParam("verifyCode")String verifyCode,
                        @RequestParam(value = "rememberMe",required = false)String rememberMe,
                        HttpSession session,HttpServletRequest request,HttpServletResponse response){
        //对三个参数判空 并返回对应的错误信息
        if (userName.isEmpty() || StringUtils.isEmpty(password)){
           session.setAttribute("errorMsg","用户名或密码不能为空"); 
           return "admin/login";
        }
        
        //判断验证码是否为空
        if (StringUtils.isEmpty(verifyCode)){
           session.setAttribute("errorMsg","验证码不能为空"); 
           return "admin/login";
        }
    
        String kaptcha = session.getAttribute("verifyCode") + "";
        //检验验证码是否正确
        if (kaptcha.isEmpty() || !verifyCode.equals(kaptcha)){
            session.setAttribute("errorMsg","验证码错误");
            return "admin/login";
        }
        
    
        //根据输入的信息找到登陆的用户
        AdminUser adminUser = adminUserService.login(userName, password);
        if (adminUser != null){
            // //如果账号密码正确 那就把用户信息存入Session
            // session.setAttribute("loginUser",adminUser.getNickName());
            // session.setAttribute("loginUserId",adminUser.getAdminUserId());
            // //设置session过期时间  一天过期
            // session.setMaxInactiveInterval(60*60*12);
    
    
            //这里的session用来在页面之间传递用户信息
            session.setAttribute("loginUser",adminUser.getNickName());
            session.setAttribute("loginUserId",adminUser.getAdminUserId());
            session.setMaxInactiveInterval(60*60*12);
            
            //判断记住密码是否开启 开启了就进行下面的cookie操作
            if (rememberMe != null && rememberMe.equals("on")){
                //使用Cookie 实现记住密码功能
                Cookie cookie_userName = new Cookie("cookie_userName",userName);
                //设置cookie过期 一个月
                cookie_userName.setMaxAge(60*60*24*30);
                //设置当前项目下的都携带这个cookie
                cookie_userName.setPath(request.getContextPath());
    
                response.addCookie(cookie_userName);
            }
           
    
            //去到主页 欢迎
            return "redirect:/admin/index";
        }else {
            session.setAttribute("errorMsg","登录失败");
            return "admin/login";
        }
    
    }
    
    /**
     * index
     */
    @GetMapping({"","/","/index","/index.html"})
    @ApiOperation("跳转后台首页")
    public String index(HttpServletRequest request){
        //path属性用于供前端告知哪个模块被选中
        request.setAttribute("path","index");
        //这里要根据其他库的数据条数获得
        request.setAttribute("categoryCount",categoryService.countCategory(null));
        request.setAttribute("blogCount",blogService.countBlog(null));
        request.setAttribute("linkCount",linkService.countLink(null));
        request.setAttribute("tagCount",tagService.countTag(null));
        request.setAttribute("commentCount",commentService.countComment(null));
        
        //存放访问人数
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());

        
        return "admin/index";
    }
    
    
    /**
     * 修改名称和密码
     */
    @GetMapping("/profile")
    @ApiOperation("跳转密码修改")
    public String profile(HttpServletRequest request){
        //通过登陆后存入session的用户信息 找到更多用户信息
        Integer loginUserId = (int)request.getSession().getAttribute("loginUserId");
        AdminUser user = adminUserService.getUserInfoById(loginUserId);
        //判断是否为空
        if (user == null){
            return "admin/login";
        }
        
        request.setAttribute("path","profile");
        request.setAttribute("loginUserName",user.getLoginUserName());
        request.setAttribute("nickName",user.getNickName());
        return "admin/profile";
    }
    
    
    /**
     * 修改密码
     */
    @PostMapping("/profile/password")
    @ResponseBody
    @ApiOperation("密码修改")
    public String passwordUpdate(@RequestParam("originalPassword") String original,
                                 @RequestParam("newPassword") String newPassword,
                                 HttpServletRequest request){
        
        //拿到新旧密码 判空
        if (original.isEmpty() || newPassword.isEmpty()){
            return "密码不能为空";
        }
        Integer userId = (int)request.getSession().getAttribute("loginUserId");
        if(adminUserService.updatePassword(userId,original,newPassword)){
            //密码更新成功 删除session中信息
            request.getSession().invalidate();
            return "success";            
        }else {
            return "修改失败";
        }
    }
    
    /**
     * 修改用户名和昵称
     */
    @RequestMapping("/profile/name")
    @ResponseBody
    @ApiOperation("用户信息修改")
    public String nameUpdate(@RequestParam("loginUserName") String loginUserName,
                             @RequestParam("nickName")String nickName,
                             HttpServletRequest request){
        //拿到数据 判空
        if (loginUserName.isEmpty() || nickName.isEmpty()){
            return "用户名或昵称不能为空";
        }
        
        Integer userId = (int)request.getSession().getAttribute("loginUserId");
        if (adminUserService.updateName(userId,loginUserName,nickName)){
            request.getSession().setAttribute("loginUser",nickName);
            return "success";
        }else {
            return "修改失败";
        }
        
    }
    
    
    /**
     * 注销
     */
    @RequestMapping("/logout")
    @ApiOperation("注销")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        request.getSession().invalidate();
        Cookie cookie_userName = new Cookie("cookie_userName", "");
        cookie_userName.setMaxAge(0);
        cookie_userName.setPath(request.getContextPath());
        response.addCookie(cookie_userName);
        return "admin/login";
    }
    
    
}
