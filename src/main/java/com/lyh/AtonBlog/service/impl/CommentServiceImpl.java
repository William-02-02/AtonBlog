package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.BlogCommentMapper;
import com.lyh.AtonBlog.pojo.BlogComment;
import com.lyh.AtonBlog.service.CommentService;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommentServiceImpl implements CommentService {
    
    @Autowired
    BlogCommentMapper blogCommentMapper;
    
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        //判断id是否为空
        if (ids.length > 0){
            return blogCommentMapper.deleteBatch(ids) >0;
        }
        return false;
    }
    
    @Override
    public Boolean replyComment(Long commentId, String reply) {
        //是否存在改评论 回复是否为空
        BlogComment blogComment = blogCommentMapper.selectByPrimaryKey(commentId);
        //这里注意判断评论状态
        if (blogComment != null && !reply.isEmpty() && blogComment.getCommentStatus() ==1){
            blogComment.setReplyBody(reply);
            blogComment.setReplyCreateTime(new Date());
            return blogCommentMapper.updateByPrimaryKey(blogComment) >0;
        }
        return false;
    }
    
    @Override
    public PageProperties getCommentListByKeyword(PageUtil pageUtil) {
        //这里的统计数量就附带了关键词
        int totalCount = blogCommentMapper.countComment(pageUtil);
        List<BlogComment> commentList = blogCommentMapper.selectAllByMap(pageUtil);
        return new PageProperties(totalCount,
                pageUtil.getPageVolume(),
                pageUtil.getCurrentPage(),
                commentList);
    }
    
    @Override
    public Boolean checkComment(int commentStatus, Long[] commentIds) {
        return blogCommentMapper.updateCommentStatusByCommentId(commentStatus,commentIds) >0;
    }
    
    @Override
    public int countComment(PageUtil pageUtil) {
        return blogCommentMapper.countComment(pageUtil);
    }
    
    //前台服务
    
    
    @Override
    public PageProperties getBlogCommentList(Long id,int page) {
        if (page <1){
            return null;
        }
        
        //评论页数由用户控制  所以要传page参数
        Map map = new HashMap();
        map.put("currentPage",page);
        map.put("blogId",id);
        map.put("pageVolume",8);
        map.put("commentStatus",1);
        PageUtil pageUtil = new PageUtil(map);
    
        List<BlogComment> blogComments = blogCommentMapper.selectByBlogId(pageUtil);
        if (!CollectionUtils.isEmpty(blogComments)){ 
            blogComments.stream()
                    .forEach(BlogComment -> BlogComment.setCommentBody(BlogComment.getCommentBody().replace("\n","<br>")));
            int total = blogCommentMapper.countByBlogId(id);
            PageProperties pageProperties = new PageProperties(total,pageUtil.getPageVolume(),pageUtil.getCurrentPage(),blogComments);
            return pageProperties;
        }
        return null;
    }
    
    @Override
    public Boolean addComment(BlogComment blogComment) {
        
        return blogCommentMapper.insertSelective(blogComment) > 0;
    }
}
