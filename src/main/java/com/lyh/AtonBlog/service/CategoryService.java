package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.BlogCategory;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;

import java.util.List;

public interface CategoryService {
    //后台管理页面服务
    
    /**
     * 对于类别的增删改查
     */

    Boolean deleteCategories(Integer[] ids);
    
    Boolean addCategory(String categoryName,String categoryIcon);
    
    // BlogCategory findCertainCategory(Long id);
    
    Boolean updateCategory(Integer categoryId,String categoryName,String categoryIcon);
    
    //这里的pageUtil用意和同下
    int countCategory(PageUtil pageUtil);
    
    //搜索关键词 找到分类 默认展示所有分类  
    //pageUtil本质上是map 只是兼顾了page属性的关系转换 
    // 这里我把需要的关键词放进去便可以得到符合关键词的List
    // 返回值原先是List<BlogCategory> 后改为PageProperties 
    // 为的是把关键词查找和查找总数以及分类封装到一个方法
    PageProperties getCategoryList(PageUtil pageUtil);
    
    List<BlogCategory> getAllCategory();
    
    
    //===================前台服务
    

    
}
