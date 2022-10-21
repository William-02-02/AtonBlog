package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.pojo.Link;
import com.lyh.AtonBlog.service.LinkService;
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
@Api(tags = "友链管理")
public class LinkController {
    
    @Autowired
    LinkService linkService;
    
    /**
     * 显示Link页面
     */
    @GetMapping("/links")
    @ApiOperation("跳转友链管理页面")
    public String linkPage(HttpServletRequest request){
        request.setAttribute("path","links");
        return "admin/link";
    }
    
    /**
     * 显示 搜索
     */
    @GetMapping("/links/list")
    @ResponseBody
    @ApiOperation("友链展示")
    public Result linkList(@RequestParam Map<String,Object> map){
        //判空
        if (StringUtils.isEmpty(map.get("currentPage")) || StringUtils.isEmpty(map.get("pageVolume"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageUtil pageUtil = new PageUtil(map);
        PageProperties linkList = linkService.findLinkListByKeyword(pageUtil);
        return ResultGenerator.genSuccessResult(linkList);
    }
    
    
    /**
     * 新增友链
     */
    @PostMapping("/links/save")
    @ResponseBody
    @ApiOperation("友链添加")
    public Result linkAdd(@RequestParam("linkType")Integer linkType,
                          @RequestParam("linkName")String linkName,
                          @RequestParam("linkUrl")String linkUrl,
                          @RequestParam("linkDescription")String linkDesc,
                          @RequestParam("linkRank")Integer linkRank){
        
        //判空
        if (linkType < 0|| linkName.isEmpty()|| linkUrl.isEmpty()|| linkDesc.isEmpty()|| linkRank <0){
            return ResultGenerator.genFailResult("参数异常");
        }
        Link link = new Link();
        link.setLinkType(linkType);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDesc);
        link.setLinkRank(linkRank);
        return ResultGenerator.genSuccessResult(linkService.addLink(link));
    
    }
    
    
    /**
     * 详情  修改页面需要先读取连接信息
     */
    @GetMapping("/links/info")
    @ResponseBody
    @ApiOperation("友链展示")
    public Result getInfor(@RequestParam("id")Integer id){
        Link link = linkService.linkDetail(id);
        if (link == null){
            return ResultGenerator.genFailResult("参数异常");
        }
        return ResultGenerator.genSuccessResult(link);
    }
    
    /**
     * 更新友链
     */
    @PostMapping("/links/update")
    @ResponseBody
    @ApiOperation("友链更新")
    public Result updateLink(@RequestParam("linkId")Integer linkId,
                             @RequestParam("linkType")Integer linkType,
                             @RequestParam("linkName")String linkName,
                             @RequestParam("linkUrl")String linkUrl,
                             @RequestParam("linkDescription")String linkDesc,
                             @RequestParam("linkRank")Integer linkRank){
        //判空
        if (linkType < 0|| linkName.isEmpty()|| linkUrl.isEmpty()|| linkDesc.isEmpty()|| linkRank <0){
            return ResultGenerator.genFailResult("参数异常");
        }
        
        Link link = new Link();
        link.setLinkId(linkId);
        link.setLinkType(linkType);
        link.setLinkName(linkName);
        link.setLinkUrl(linkUrl);
        link.setLinkDescription(linkDesc);
        link.setLinkRank(linkRank);
        
        return linkService.updateLink(link)? ResultGenerator.genSuccessResult(true):ResultGenerator.genFailResult("更新失败");
    }
    
    
    /**
     * 删除连接
     */
    @PostMapping("/links/delete")
    @ResponseBody
    @ApiOperation("友链删除")
    public Result deleteLink(@RequestBody Integer[] ids){
        
        if (ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (linkService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
        
    }
        
    
    
    
}
