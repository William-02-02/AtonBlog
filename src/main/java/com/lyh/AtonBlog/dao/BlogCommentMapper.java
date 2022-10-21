package com.lyh.AtonBlog.dao;
import java.util.List;
import java.util.Map;

import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import com.lyh.AtonBlog.pojo.BlogComment;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.lyh.AtonBlog.pojo.BlogComment
 */
@Repository
public interface BlogCommentMapper {

    int deleteByPrimaryKey(Long id);

    int insert(BlogComment record);

    int insertSelective(BlogComment record);

    BlogComment selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BlogComment record);

    int updateByPrimaryKey(BlogComment record);
    
    int countComment(PageUtil pageUtil);
    
    int countByBlogId(@Param("blogId") Long blogId);

    int deleteBatch(Integer[] ids);
    
    List<BlogComment> selectAllByMap(PageUtil pageUtil);
    
    int updateCommentStatusByCommentId(@Param("commentStatus") int commentStatus, @Param("commentIds") Long[] commentIds);
    
    //前台服务
    List<BlogComment> selectByBlogId(PageUtil pageUtil);
}
