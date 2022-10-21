package com.lyh.AtonBlog.controller.admin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.lyh.AtonBlog.config.Constants;
import com.lyh.AtonBlog.pojo.Blog;
import com.lyh.AtonBlog.service.BlogService;
import com.lyh.AtonBlog.service.CategoryService;
import com.lyh.AtonBlog.util.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.core.appender.rewrite.MapRewritePolicy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.view.InternalResourceViewResolver;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Controller
@RequestMapping("/admin")
@Api(tags = "博客管理")
public class BlogController {
    
    @Autowired
    BlogService blogService;
    
    @Autowired
    CategoryService categoryService;
    
    
    /**
     * 跳转 blog页面
     */
    @GetMapping("/blogs")
    @ApiOperation("跳转博客管理页面")
    public String blogPage(HttpServletRequest request){
        request.setAttribute("path","blogs");
        return "admin/blog";
    }
    
    /**
     * 展示 bloglist
     */
    @GetMapping("/blogs/list")
    @ResponseBody
    @ApiOperation("博客展示")
    public Result blogList(@RequestParam Map<String,Object> map){
        if (StringUtils.isEmpty(map.get("currentPage")) || StringUtils.isEmpty(map.get("pageVolume"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageUtil pageUtil = new PageUtil(map);
        PageProperties blogList = blogService.getBlogByKeyword(pageUtil);
        return ResultGenerator.genSuccessResult(blogList);
    
    }
    
    /**
     * 新增页面
     */
    @GetMapping("/blogs/edit")
    @ApiOperation("跳转编辑")
    public String blogEdit(HttpServletRequest request){
        request.setAttribute("path","edit");
        request.setAttribute("categories",categoryService.getAllCategory());
        return "admin/edit";
    }
    
    /**
     * 新增
     */
    @PostMapping("/blogs/save")
    @ResponseBody
    @ApiOperation("博客保存")
    public Result blogSave(@RequestParam("blogTitle")String blogTitle,
                           @RequestParam(name = "blogSubUrl",required = false)String blogSubUrl,
                           @RequestParam("blogCategoryId")Integer blogCategoryId,
                           @RequestParam("blogTags")String blogTags,
                           @RequestParam("blogContent")String blogContent,
                           @RequestParam("blogCoverImage")String blogCoverImage,
                           @RequestParam("blogStatus")Byte blogStatus,
                           @RequestParam("enableComment")Byte enableComment){
        //判空
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
        
        
        //封装信息
        Blog blog = new Blog();
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
    
        String message = blogService.saveBlog(blog);
        if (message.equals("success")){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(message);
        }
        
    }
    
    
    /**
     * 更新博客
     */
    @PostMapping("/blogs/update")
    @ResponseBody
    @ApiOperation("博客更新")
    public Result blogUpdate(@RequestParam("blogId")Long blogId,
                             @RequestParam("blogTitle")String blogTitle,
                             @RequestParam(name = "blogSubUrl",required = false)String blogSubUrl,
                             @RequestParam("blogCategoryId")Integer blogCategoryId,
                             @RequestParam("blogTags")String blogTags,
                             @RequestParam("blogContent")String blogContent,
                             @RequestParam("blogCoverImage")String blogCoverImage,
                             @RequestParam("blogStatus")Byte blogStatus,
                             @RequestParam("enableComment")Byte enableComment){
        //判空
        if (blogId < 0){
            return ResultGenerator.genFailResult("博客Id异常");
        }
        if (StringUtils.isEmpty(blogTitle)) {
            return ResultGenerator.genFailResult("请输入文章标题");
        }
        if (blogTitle.trim().length() > 150) {
            return ResultGenerator.genFailResult("标题过长");
        }
        if (StringUtils.isEmpty(blogTags)) {
            return ResultGenerator.genFailResult("请输入文章标签");
        }
        if (blogTags.trim().length() > 150) {
            return ResultGenerator.genFailResult("标签过长");
        }
        if (blogSubUrl.trim().length() > 150) {
            return ResultGenerator.genFailResult("路径过长");
        }
        if (StringUtils.isEmpty(blogContent)) {
            return ResultGenerator.genFailResult("请输入文章内容");
        }
        if (blogTags.trim().length() > 100000) {
            return ResultGenerator.genFailResult("文章内容过长");
        }
        if (StringUtils.isEmpty(blogCoverImage)) {
            return ResultGenerator.genFailResult("封面图不能为空");
        }
    
    
        //封装信息
        Blog blog = new Blog();
        blog.setBlogId(blogId);
        blog.setBlogTitle(blogTitle);
        blog.setBlogSubUrl(blogSubUrl);
        blog.setBlogCategoryId(blogCategoryId);
        blog.setBlogTags(blogTags);
        blog.setBlogContent(blogContent);
        blog.setBlogCoverImage(blogCoverImage);
        blog.setBlogStatus(blogStatus);
        blog.setEnableComment(enableComment);
    
        String message = blogService.updateBlog(blog);
        if (message.equals("success")){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult(message);
        }
    
    }
    
    
    /**
     * 获得待更新博客信息
     */
    @GetMapping("/blogs/edit/{blogId}")
    @ApiOperation("获取博客信息")
    public String getBlogInfo(HttpServletRequest request,@PathVariable("blogId")Long blogId){
        
        request.setAttribute("path","edit");
    
        Blog blog = blogService.getBlog(blogId);
        //判空
        if (blog ==null){
            return "error/error_400";
        }
        request.setAttribute("blog",blog);
        request.setAttribute("categories",categoryService.getAllCategory());
        return "admin/edit";
    
    }
    
    
    /**
     * 删除博客
     */
    @PostMapping("/blogs/delete")
    @ResponseBody
    @ApiOperation("博客删除")
    public Result deletBlog(@RequestBody Integer[] ids){
        if (ids.length < 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        
        if (blogService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
        
    }
    
    /**
     * md中图片上传
     */
    @PostMapping("/blogs/md/uploadfile")
    @ResponseBody
    @ApiOperation("md图片上传")
    public JSONObject upload(HttpServletRequest request, 
                         HttpServletResponse response,
                         @RequestParam(value = "editormd-image-file",required = true) MultipartFile file) throws URISyntaxException, IOException {
        String fileName = file.getOriginalFilename();
        //获取.后面的文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        //文件以日期+随机数+后缀的形式命名
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //注意 这里new File只是创建了一个file对象 并没有写入到真实路径中
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        //把uri中的信息封装到响应中 用于前台显示上传的图片
        String fileUrl = MyBlogUtils.getHost(new URI(request.getRequestURL() + "")) + "/upload/" + newFileName;
    
        JSONObject jsonObject = new JSONObject();
        try {
            if (!fileDirectory.exists()) {
                //文件夹不存在就新建
                if (!fileDirectory.mkdirs()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            //把文件转移到目标处
            file.transferTo(destFile);
            //封装响应信息
            request.setCharacterEncoding("utf-8");
            // response.setHeader("Content-Type", "text/html");
            // response.getWriter().write("{\"success\": 1, \"message\":\"success\",\"url\":\"" + fileUrl + "\"}");
            jsonObject.put("success",1);
            jsonObject.put("message","success");
            jsonObject.put("url",fileUrl);
            return jsonObject;
        } catch (IOException e) {
            // response.getWriter().write("{\"success\":0}");
            e.printStackTrace();
        }
        jsonObject.put("message",0);
        return jsonObject;
    }
    
    
    
}
