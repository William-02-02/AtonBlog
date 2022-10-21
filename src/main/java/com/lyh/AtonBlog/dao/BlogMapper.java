package com.lyh.AtonBlog.dao;

import com.lyh.AtonBlog.pojo.Blog;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Entity com.lyh.AtonBlog.pojo.Blog
 */
@Repository
public interface BlogMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Blog record);

    int insertSelective(Blog record);

    Blog selectByPrimaryKey(Long id);
    
    List<Blog> selectAllByBlogIdBatch(Integer[] ids);
    
    Blog selectByBlogSubUrl(@Param("blogSubUrl") String blogSubUrl);
    
    int updateByPrimaryKeySelective(Blog record);

    int updateByPrimaryKey(Blog record);
    
    
    /**
     * 后续根据需要添加的
     */
    //批量删除
    int deleteBatch(Integer[] ids);
    
    //关键词查找
    List<Blog> findBlogListByMap(PageUtil pageUtil);
    
    //统计数量
    
    int countBlog(PageUtil pageUtil);
    
    Blog selectByBlogTitle(@Param("blogTitle") String blogTitle);
    
    
    /**
     * 删除分类时需要用到的 批量更改blog分类的方法 更新
     * @param categoryName 需要更新到的分类名
     * @param categoryId  需要更新到的分类id
     * @param ids 需要更新的分类id
     * @return
     */
    int updateBlogCategories(@Param("categoryName")String categoryName,
                             @Param("categoryId") Integer categoryId,
                             @Param("ids") Integer[] ids);
    
    /**
     * 根据类型获取 最高点击 最新发布
     */
    List<Blog> selectBlogListByType(@Param("type") int type,@Param("limit") int limit);
    
    /**
     * 根据标签获取博客列表
     */
    List<Blog> selectBlogPageByTagId(PageUtil pageUtil);
    
    /**
     * 根据标签id获得博客数量
     */
    int getBlogCountByTagId(int tagId);
    
    /**
     * 根据分类id获得博客数量
     */
    int getBlogCountByCategoryId(int categoryId);
    
    
}
