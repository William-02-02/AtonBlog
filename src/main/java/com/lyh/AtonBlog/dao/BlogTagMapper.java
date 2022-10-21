package com.lyh.AtonBlog.dao;
import com.lyh.AtonBlog.pojo.BlogTagCount;
import com.lyh.AtonBlog.pojo.BlogTagRelation;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;

import com.lyh.AtonBlog.pojo.BlogTag;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.lyh.AtonBlog.pojo.BlogTag
 */
@Repository
public interface BlogTagMapper {

    int deleteByPrimaryKey(Long id);

    int insert(BlogTag record);

    int insertSelective(BlogTag record);
    
    int insertByTagName(String tagName);

    BlogTag selectByPrimaryKey(Long id);
    
    BlogTag selectByTagName(String tagName);

    int updateByPrimaryKeySelective(BlogTag record);

    int updateByPrimaryKey(BlogTag record);
    
    List<BlogTag> getTagList(PageUtil pageUtil);
    
    int deleteBatch(Integer[] ids);
    
    int getTotalTagCount(PageUtil pageUtil);
    
    int BatchInsertTagList(List<BlogTag> blogTags);
    
    //获取标签以及其数量
    List<BlogTagCount> getTagWithCount();

}
