package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.service.TagService;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Api(tags = "标签管理")
public class TagController {
    
    @Autowired
    TagService tagService;
    
    @GetMapping("/tags")
    @ApiOperation("跳转标签页面")
    public String showTagPage(HttpServletRequest request){
        request.setAttribute("path","tags");
        return "admin/tag";
    }
    
    @GetMapping("/tags/list")
    @ResponseBody
    @ApiOperation("标签展示")
    public Result tagList(@RequestParam Map<String,Object> map){
        //前端传递的参数进行判空
        if (StringUtils.isEmpty(map.get("currentPage")) || StringUtils.isEmpty(map.get("pageVolume"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageUtil pageUtil = new PageUtil(map);
        return ResultGenerator.genSuccessResult(tagService.getTagListWithKeyword(pageUtil));
    }
    
    @PostMapping("/tags/save")
    @ResponseBody
    @ApiOperation("标签添加")
    public Result addTag(@RequestParam("tagName")String tagName){
        //判空 判断是否重复 
        //这里判断是否重复在Service层做过了 所以如果返回false则代表重复了。
        if (tagName.isEmpty()){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (tagService.addTag(tagName)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("标签重复");
        }
    }
    
    @PostMapping("/tags/delete")
    @ResponseBody
    @ApiOperation("标签删除")
    public Result deleteTag(@RequestBody Integer[] ids){
        
        if (ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (tagService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }
    
    
    
    
}
