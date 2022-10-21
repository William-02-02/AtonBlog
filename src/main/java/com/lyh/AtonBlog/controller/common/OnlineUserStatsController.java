package com.lyh.AtonBlog.controller.common;

import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.time.Duration;

@Controller
@RequestMapping("/onlineUserStats")
public class OnlineUserStatsController {
    
    @Autowired
    OnlineUserStatisticsService onlineUserStatisticsService;
    
    @RequestMapping("/onlineUserCount")
    public Result onlineUserCount(HttpServletRequest request){
    
        //清理时间超过2分钟的value
        onlineUserStatisticsService.clear(Duration.ofMinutes(2));
        //统计获得在线人数
        Long count = onlineUserStatisticsService.count();
    
        return ResultGenerator.genSuccessResult(count);
    }
    
    @RequestMapping("resetTime")
    public void resetTime(HttpServletRequest request){
        
    }
    
}
