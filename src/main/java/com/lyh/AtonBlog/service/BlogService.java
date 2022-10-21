package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.controller.vo.BlogDetailVO;
import com.lyh.AtonBlog.controller.vo.SimpleBlogListVO;
import com.lyh.AtonBlog.pojo.Blog;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface BlogService {
    //=============纯后台服务=============
    /**
     * 新增博客
     */
    String saveBlog(Blog blog);
    
    /**
     * 更新博客
     */
    String updateBlog(Blog blog);
    
    /**
     * 删除博客
     */
    Boolean deleteBatch(Integer[] ids);
    
    /**
     * 根据关键词查找
     */
    PageProperties getBlogByKeyword(PageUtil pageUtil);
    
    int countBlog(PageUtil pageUtil);
    
    Blog getBlog(Long id);
     
    //=============前台服务============
    /**
     * 前台博客数据查询 分页 博客显示
     */
    PageProperties getBlogsForIndexPage(int page);
    
    /**
     * 侧边 最新发布和点击最多
     */
    List<SimpleBlogListVO> getSimpleBlogListForIndexPage(int type);
    
    /**
     * 封装blog对象
     * 获得blogDetail
     */
    BlogDetailVO getBlogDetailVO(Blog blog);
    
    /**
     * 根据id获得 BlogDetailVO
     */
    BlogDetailVO getBlogDetailById(Long id);
    
    /**
     * 根据 subUrl 获得BlogDetailVO
     */
    BlogDetailVO getBlogDetailBySubUrl(String subUrl);
    
    /**
     * 根据 tag 获取 blogList
     */
    PageProperties getBlogPageByTag(String tagName,int page);
    
    /**
     * 根据 category 获取 blogList
     */
    PageProperties getBlogPageByCategory(String categoryName,int page);
    
    
    /**
     * 根据 keyword 获取 blogList
     */
    PageProperties getBlogPageBySearch(String keyword,int page);
    

    
}
