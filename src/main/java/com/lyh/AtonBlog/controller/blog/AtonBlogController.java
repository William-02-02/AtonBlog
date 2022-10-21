package com.lyh.AtonBlog.controller.blog;

import com.lyh.AtonBlog.controller.common.OnlineUserStatisticsService;
import com.lyh.AtonBlog.controller.vo.BlogDetailVO;
import com.lyh.AtonBlog.pojo.BlogCategory;
import com.lyh.AtonBlog.pojo.BlogComment;
import com.lyh.AtonBlog.pojo.Link;
import com.lyh.AtonBlog.service.*;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PatternUtil;
import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

@Controller
@Api(tags = "前台控制器")
public class AtonBlogController {
    public static String theme = "amaze"; 
    
    @Autowired
    BlogService blogService;
    
    @Autowired
    ConfigService configService;
    
    @Autowired
    CategoryService categoryService;
    
    @Autowired
    TagService tagService;
    
    @Autowired
    CommentService commentService;
    
    @Autowired
    LinkService linkService;
    
    @Autowired
    OnlineUserStatisticsService onlineUserStatsService;
    
    
    /**
     * 请求首页，在显示之前需要封装信息 所以这里调用page。
     */
    @GetMapping({"/","/index","/index.html"})
    public String index(HttpServletRequest request){
        return this.page(request,1);
    }
    
    /**
     * 首页 分页数据
     */
    @GetMapping("/page/{pageNum}")
    @ApiOperation("首页展示")
    public String page(HttpServletRequest request, @PathVariable("pageNum")int pageNum){
        //获取前端所需数据
        PageProperties blogsForIndexPage = blogService.getBlogsForIndexPage(pageNum);
        if (blogsForIndexPage == null){
            return "error/error_404";
        }
        //封装前端所需数据
        request.setAttribute("blogPageResult",blogsForIndexPage);
        request.setAttribute("newBlogs",blogService.getSimpleBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getSimpleBlogListForIndexPage(0));
        request.setAttribute("hotTags",tagService.getHotTagWithCount());
        //新增分类模块
        request.setAttribute("hotCategories",categoryService.getAllCategory());
        request.setAttribute("pageName","首页");
        request.setAttribute("configurations",configService.getAllConfig());
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());

        
        return "blog/"+theme+"/index";
    }
    
    /**
     * categories 页面 包括分类数据和标签数据
     */
    @GetMapping("/categories")
    @ApiOperation("分类页面")
    public String categories(HttpServletRequest request){
        //封装数据
        
        request.setAttribute("hotTags",tagService.getHotTagWithCount());
        request.setAttribute("categories",categoryService.getAllCategory());
        request.setAttribute("pageName","分类页面");
        request.setAttribute("configurations",configService.getAllConfig());
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());
        return "blog/"+theme+"/category";
    }
    
    
    /**
     * 详情页面
     */
    @GetMapping({"/blog/{blogId}","/article/{blogId}"})
    @ApiOperation("博客详情")
    public String blogDetail(HttpServletRequest request, @PathVariable("blogId")Long blogId, 
                             @RequestParam(value = "commentPage",required = false,defaultValue = "1")int page){
        //这里的page用来接收评论页码
        BlogDetailVO blogDetailVO = blogService.getBlogDetailById(blogId);
        if (blogDetailVO != null){
            request.setAttribute("blogDetailVO",blogDetailVO);
            request.setAttribute("commentPageResult",commentService.getBlogCommentList(blogId,page));
        }
        request.setAttribute("pageName","详情");
        request.setAttribute("configurations",configService.getAllConfig());
        return "blog/"+theme+"/detail";
    }
    
    
    /**
     * 标签页面
     */
    //点击名字 默认跳到这个请求 展示第一页
    @GetMapping({"/tag/{tagName}"})
    public String tag(HttpServletRequest request,@PathVariable("tagName")String tagName){
        return tag(request,tagName,1);
    }
    
    //下一页会直接调用这个方法
    @GetMapping({"/tag/{tagName}/{page}"})
    @ApiOperation("标签侧边栏")
    public String tag(HttpServletRequest request, @PathVariable("tagName")String tagName,
                             @PathVariable("page")Integer page) {
        
        PageProperties blogPageByTag = blogService.getBlogPageByTag(tagName, page);
        request.setAttribute("blogPageResult",blogPageByTag);
        request.setAttribute("pageName","标签");
        request.setAttribute("pageUrl","tag");
        request.setAttribute("keyword",tagName);
        request.setAttribute("newBlogs",blogService.getSimpleBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getSimpleBlogListForIndexPage(0));
        request.setAttribute("hotCategories",categoryService.getAllCategory());
        request.setAttribute("hotTags",tagService.getHotTagWithCount());
        request.setAttribute("configurations",configService.getAllConfig());
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());
        return "blog/"+theme+"/list";
    }
    
    
    /**
     * 分类页面
     */
    @GetMapping({"/category/{categoryName}"})
    public String category(HttpServletRequest request,@PathVariable("categoryName")String categoryName){
        return category(request,categoryName,1);
    }
    
    @GetMapping({"/category/{categoryName}/{page}"})
    @ApiOperation("分类侧边栏")
    public String category(HttpServletRequest request, @PathVariable("categoryName")String categoryName,
                      @PathVariable("page")Integer page) {
        
        PageProperties blogPageByCategory = blogService.getBlogPageByCategory(categoryName, page);
        request.setAttribute("blogPageResult",blogPageByCategory);
        request.setAttribute("pageName","标签");
        request.setAttribute("pageUrl","tag");
        request.setAttribute("keyword",categoryName);
        request.setAttribute("newBlogs",blogService.getSimpleBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getSimpleBlogListForIndexPage(0));
        request.setAttribute("hotCategories",categoryService.getAllCategory());
        request.setAttribute("hotTags",tagService.getHotTagWithCount());
        request.setAttribute("configurations",configService.getAllConfig());
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());
        return "blog/"+theme+"/list";
    }
    
    
    /**
     * 搜索页面
     */
    @GetMapping({"/search/{keyword}"})
    public String search(HttpServletRequest request,@PathVariable("keyword")String keyword){
        return search(request,keyword,1);
    }
    
    @GetMapping({"/search/{keyword}/{page}"})
    @ApiOperation("搜索功能")
    public String search(HttpServletRequest request, @PathVariable("keyword")String keyword,
                           @PathVariable("page")Integer page) {
        
        PageProperties blogPageByKeyword = blogService.getBlogPageBySearch(keyword, page);
        request.setAttribute("blogPageResult",blogPageByKeyword);
        request.setAttribute("pageName","标签");
        request.setAttribute("pageUrl","tag");
        request.setAttribute("keyword",keyword);
        request.setAttribute("newBlogs",blogService.getSimpleBlogListForIndexPage(1));
        request.setAttribute("hotBlogs",blogService.getSimpleBlogListForIndexPage(0));
        request.setAttribute("hotCategories",categoryService.getAllCategory());
        request.setAttribute("hotTags",tagService.getHotTagWithCount());
        request.setAttribute("configurations",configService.getAllConfig());
        request.setAttribute("onlineUserStats",onlineUserStatsService.count());
        return "blog/"+theme+"/list";
    }
    
    /**
     * 友链页面
     */
    @GetMapping("/link")
    @ApiOperation("友链页面")
    public String link(HttpServletRequest request){
        request.setAttribute("pageName","友情链接");
        Map<Integer, List<Link>> linkMap = linkService.getLinksForLinkPage();
        if (linkMap != null){
            //如果不判断 容易空指针  0-友链 1-推荐 2-个人网站
            if (linkMap.containsKey(0)){
                request.setAttribute("favoriteLinks",linkMap.get(0));
            }
            if (linkMap.containsKey(1)){
                request.setAttribute("recommendLinks",linkMap.get(1));
            }
            if (linkMap.containsKey(2)){
                request.setAttribute("personalLinks",linkMap.get(2));
            }
        }
        request.setAttribute("configurations",configService.getAllConfig());
        return "blog/"+theme+"/link";
    
    }
    
    
    /**
     * 评论
     */
    @PostMapping("/blog/comment")
    @ResponseBody
    @ApiOperation("底部评论")
    public Result comment(HttpServletRequest request, HttpSession session,
                          @RequestParam Long blogId, @RequestParam String verifyCode,
                          @RequestParam String commentator, @RequestParam String email,
                          @RequestParam String websiteUrl, @RequestParam String commentBody){
        if (verifyCode.isEmpty()){
            return ResultGenerator.genFailResult("验证码不能为空");
        }
        String kaptchaCode = session.getAttribute("verifyCode") + "";
        if (kaptchaCode.isEmpty()){
            return ResultGenerator.genFailResult("非法请求");
        }
        if (!verifyCode.equals(kaptchaCode)) {
            return ResultGenerator.genFailResult("验证码错误");
        }
        String ref = request.getHeader("Referer");
        if (StringUtils.isEmpty(ref)) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (null == blogId || blogId < 0) {
            return ResultGenerator.genFailResult("非法请求");
        }
        if (StringUtils.isEmpty(commentator)) {
            return ResultGenerator.genFailResult("请输入称呼");
        }
        if (StringUtils.isEmpty(email)) {
            return ResultGenerator.genFailResult("请输入邮箱地址");
        }
        if (!PatternUtil.isEmail(email)) {
            return ResultGenerator.genFailResult("请输入正确的邮箱地址");
        }
        if (StringUtils.isEmpty(commentBody)) {
            return ResultGenerator.genFailResult("请输入评论内容");
        }
        if (commentBody.trim().length() > 200) {
            return ResultGenerator.genFailResult("评论内容过长");
        }
        BlogComment blogComment = new BlogComment();
        blogComment.setBlogId(blogId);
        blogComment.setCommentator(commentator);
        blogComment.setEmail(email);
        if (!websiteUrl.isEmpty() && PatternUtil.isURL(websiteUrl)){
            blogComment.setWebsiteUrl(websiteUrl);
        }else if (!websiteUrl.isEmpty()){
            return ResultGenerator.genFailResult("网站格式错误");
        }
        blogComment.setCommentBody(commentBody);
        return ResultGenerator.genSuccessResult(commentService.addComment(blogComment));
    
    }
    
    /**
     * 关于页面
     */
    @GetMapping("/{subUrl}")
    @ApiOperation("subUrl与About页面")
    public String detail(HttpServletRequest request,@PathVariable("subUrl")String subUrl){
        BlogDetailVO blogDetailVO = blogService.getBlogDetailBySubUrl(subUrl);
        if (blogDetailVO != null){
            request.setAttribute("blogDetailVO",blogDetailVO);
            request.setAttribute("pageName",subUrl);
            request.setAttribute("configurations",configService.getAllConfig());
            return "blog/"+theme+"/detail";
        }
        return "error/error_400";
    }
    
    
}
