package com.lyh.AtonBlog.controller.admin;


import com.lyh.AtonBlog.service.ConfigService;
import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.core.appender.rolling.action.IfAll;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
@Api(tags = "设置管理")
public class ConfigController {
    
    @Autowired
    ConfigService configService;
    
    @GetMapping("/configurations")
    @ApiOperation("跳转设置页面")
    public String configPage(HttpServletRequest request){
        request.setAttribute("path","configurations");
        //这里显示页面的同时需要把数据也传过去 
        //因为前段是使用Thymeleaf获取数据的 而不是ajax 不会发一个请求
        request.setAttribute("configurations",configService.getAllConfig());
        return "admin/configuration";
    }
    
    @PostMapping("/configurations/website")
    @ResponseBody
    @ApiOperation("网站设置更新")
    public Result website(@RequestParam(value = "websiteName") String websiteName,
                          @RequestParam(value = "websiteDescription") String websiteDescription,
                          @RequestParam("websiteLogo") String websiteLogo,
                          @RequestParam("websiteIcon") String websiteIcon){
    
        HashMap<String, String> map = new HashMap<>();
        //判空 分别更新
        if (websiteName.isEmpty()){
            return ResultGenerator.genFailResult("网站名不能为空");
        }
        if (websiteDescription.isEmpty()){
            return ResultGenerator.genFailResult("网站描述不能为空");
        }
        if (websiteLogo.isEmpty()){
            return ResultGenerator.genFailResult("网站Logo不能为空");
        }
        if (websiteIcon.isEmpty()){
            return ResultGenerator.genFailResult("网站Icon不能为空");
        }
    
        map.put("websiteName",websiteName);
        map.put("websiteIcon",websiteIcon);
        map.put("websiteLogo",websiteLogo);
        map.put("websiteDescription",websiteDescription);
        
        return ResultGenerator.genSuccessResult(configService.updateConfig(map));
    }
    
    @PostMapping("/configurations/userInfo")
    @ResponseBody
    @ApiOperation("用户设置更新")
    public Result website(@RequestParam("yourAvatar") String yourAvatar,
                          @RequestParam("yourName") String yourname,
                          @RequestParam("yourEmail") String yourEmail){
        //判空 分别更新
        if (yourAvatar.isEmpty()){
            return ResultGenerator.genFailResult("头像不能为空");
        }
        if (yourname.isEmpty()){
            return ResultGenerator.genFailResult("名称不能为空");
        }
        if (yourEmail.isEmpty()){
            return ResultGenerator.genFailResult("邮箱不能为空");
        }
        
        HashMap<String, String> map = new HashMap<>();
        map.put("yourAvatar",yourAvatar);
        map.put("yourName",yourname);
        map.put("yourEmail",yourEmail);
        
        return ResultGenerator.genSuccessResult(configService.updateConfig(map));
    }
    
    
    
    @PostMapping("/configurations/footer")
    @ResponseBody
    @ApiOperation("首尾信息更新")
    public Result website(@RequestParam("footerAbout") String footerAbout,
                          @RequestParam("footerICP") String footerICP,
                          @RequestParam("footerCopyRight") String footerCopyRight,
                          @RequestParam("footerPoweredBy") String footerPoweredBy,
                          @RequestParam("footerPoweredByURL") String footerPoweredByURL
                          ){
        //判空 分别更新
        if (footerAbout.isEmpty()){
            return ResultGenerator.genFailResult("底部About不能为空");
        }
        if (footerICP.isEmpty()){
            return ResultGenerator.genFailResult("底部备案号不能为空");
        }
        if (footerCopyRight.isEmpty()){
            return ResultGenerator.genFailResult("底部CopyRight不能为空");
        }
        if (footerPoweredBy.isEmpty()){
            return ResultGenerator.genFailResult("底部PoweredBy不能为空");
        }
        if (footerPoweredByURL.isEmpty()){
            return ResultGenerator.genFailResult("底部PoweredByURL不能为空");
        }
        
        HashMap<String, String> map = new HashMap<>();
        map.put("footerAbout",footerAbout);
        map.put("footerICP",footerICP);
        map.put("footerCopyRight",footerCopyRight);
        map.put("footerPoweredBy",footerPoweredBy);
        map.put("footerPoweredByURL",footerPoweredByURL);
        
        return ResultGenerator.genSuccessResult(configService.updateConfig(map));
    }
    
    
    
}
