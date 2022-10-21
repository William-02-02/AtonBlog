package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.BlogComment;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

public interface CommentService {
    //删除评论
    Boolean deleteBatch(Integer[] ids);
    
    //回复评论
    Boolean replyComment(Long commentId ,String reply);
    
    //查找评论List /关键词
    PageProperties getCommentListByKeyword(PageUtil pageUtil);
    
    //审核评论 或撤销批准 可以批量操作
    Boolean checkComment(int commentStatus, Long[] commentIds);
    
    //获得comment数量
    int countComment(PageUtil pageUtil);
    
    /**
     * 前台服务
     */
    //获得指定博客评论list
    PageProperties getBlogCommentList(Long id,int page);
    
    //添加评论
    Boolean addComment(BlogComment blogComment);
    
}
