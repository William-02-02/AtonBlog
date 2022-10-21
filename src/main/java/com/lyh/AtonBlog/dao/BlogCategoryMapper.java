package com.lyh.AtonBlog.dao;

import com.lyh.AtonBlog.pojo.BlogCategory;
import com.lyh.AtonBlog.service.CategoryService;
import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.lyh.AtonBlog.pojo.BlogCategory
 */
@Repository
public interface BlogCategoryMapper {

    int deleteByPrimaryKey(Long id);
    
    int deleteBatch(Integer[] ids);

    int insert(BlogCategory record);

    int insertSelective(BlogCategory record);

    BlogCategory selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(BlogCategory record);

    int updateByPrimaryKey(BlogCategory record);
    
    /**
     * 后续添加的接口
     */
    BlogCategory selectByCategoryName(String categoryName);
    
    /**
     * 这里pageUtil本质上是一个map 我这里想添加一个分类搜索关键词功能
     * 这个方法就是用来获得搜索结果条数的
     */
    int countCategory(PageUtil pageUtil);
    
    List<BlogCategory> findCategoryListByKeyword(PageUtil pageUtil);
    
    int updateRankByCategoryId(@Param("map") Map<Integer,Integer> map);
    
    
    //前台使用
    /**
     * 根据分类id数组 找到分类对象
     */
    List<BlogCategory> selectAllByCategoryIds(List<Integer> ids);
    
   

}
