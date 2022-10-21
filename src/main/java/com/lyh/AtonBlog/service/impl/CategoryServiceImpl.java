package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.BlogCategoryMapper;
import com.lyh.AtonBlog.dao.BlogMapper;
import com.lyh.AtonBlog.pojo.BlogCategory;
import com.lyh.AtonBlog.service.CategoryService;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    
    @Autowired
    BlogCategoryMapper blogCategoryMapper;
    
    @Autowired
    BlogMapper blogMapper;
    
    @Override
    @Transactional
    public Boolean deleteCategories(Integer[] ids) {
        //判断id的数量是否合法
        if (ids.length < 1){
            return false;
        }
        
        //删除对应分组中的blog（把相应分组中的blog分组设置成默认分组）
        blogMapper.updateBlogCategories("Default",0,ids);
        //删除分组
        return blogCategoryMapper.deleteBatch(ids) >0;

    }
    
    @Override
    @Transactional
    public Boolean addCategory(String categoryName, String categoryIcon) {
        //判断是否已经存在重名的分类
        BlogCategory temp = blogCategoryMapper.selectByCategoryName(categoryName);
        if (temp == null){
            //不存在改名字的分组 才添加
            BlogCategory categoryReadyToAdd = new BlogCategory();
            categoryReadyToAdd.setCategoryName(categoryName);
            categoryReadyToAdd.setCategoryIcon(categoryIcon);
            return blogCategoryMapper.insertSelective(categoryReadyToAdd) > 0;
        }
        //分组以及存在了
        return false;
    }
    
    // @Override
    // public BlogCategory findCertainCategory(Long id) {
    //     return null;
    // }
    
    @Override
    @Transactional
    public Boolean updateCategory(Integer categoryId, String categoryName, String categoryIcon) {
        //判断是否存在该分组
        BlogCategory blogCategory = blogCategoryMapper.selectByPrimaryKey(categoryId);
        if (blogCategory != null){
            blogCategory.setCategoryName(categoryName); 
            blogCategory.setCategoryIcon(categoryIcon);
           //修改分类信息后 把原分类中的blog的分类信息进行修改
            blogMapper.updateBlogCategories(categoryName,blogCategory.getCategoryId(),new Integer[]{categoryId});
            return blogCategoryMapper.updateByPrimaryKeySelective(blogCategory) > 0;
        }
    
        return false;
    }
    
    /**
     * 兼顾关键词查找（查找全部） 分页信息 
     */
    @Override
    public PageProperties getCategoryList(PageUtil pageUtil) {
        //获得带有关键词的搜素结果（有无都可以）
        List<BlogCategory> categoryList = blogCategoryMapper.findCategoryListByKeyword(pageUtil);
        //获得结果总条数
        int totalCount = countCategory(pageUtil);
        PageProperties pageResult = new PageProperties(totalCount,
                pageUtil.getPageVolume(),
                pageUtil.getCurrentPage(),
                categoryList);
    
        return pageResult;
    }
    
    @Override
    public List<BlogCategory> getAllCategory() {
        return blogCategoryMapper.findCategoryListByKeyword(null);
    }
    
    @Override
    public int countCategory(PageUtil pageUtil) {
        return blogCategoryMapper.countCategory(pageUtil);
    }
}
