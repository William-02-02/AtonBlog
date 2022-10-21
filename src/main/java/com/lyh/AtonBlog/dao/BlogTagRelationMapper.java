package com.lyh.AtonBlog.dao;
import org.apache.ibatis.annotations.Param;
import java.util.Collection;
import java.util.List;

import com.lyh.AtonBlog.pojo.BlogTagRelation;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.lyh.AtonBlog.pojo.BlogTagRelation
 */
@Repository
public interface BlogTagRelationMapper {

    int deleteByPrimaryKey(Long id);

    int insert(BlogTagRelation record);

    int insertSelective(BlogTagRelation record);

    BlogTagRelation selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(BlogTagRelation record);

    int updateByPrimaryKey(BlogTagRelation record);

    int deleteBatch(Integer[] ids);
    
    int deleteBatchByBlogId(Integer[] ids);
    
    int insertBatch(@Param("blogTagRelationCollection") List<BlogTagRelation> blogTagRelationCollection);

}
