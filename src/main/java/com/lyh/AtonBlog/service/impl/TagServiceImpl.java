package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.BlogTagMapper;
import com.lyh.AtonBlog.pojo.BlogTag;
import com.lyh.AtonBlog.pojo.BlogTagCount;
import com.lyh.AtonBlog.service.TagService;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService {
    
    @Autowired
    BlogTagMapper blogTagMapper;
    
    // @Autowired
    // BlogTagRelationMapper relationMapper;
    
    @Override
    public Boolean addTag(String tagName) {
        //判空 与判断是否重复整合到一起
        BlogTag temp = blogTagMapper.selectByTagName(tagName);
        if (temp == null){
            //说明不存在这个标签
            BlogTag blogTag = new BlogTag();
            blogTag.setTagName(tagName);
            return blogTagMapper.insertSelective(blogTag) > 0;   
        }
        return false;
    }
    
    @Override
    public Boolean updateTag(String oldTagName, String newTagName) {
        //判空 并判断旧名字是否存在 新名字是否可行
        BlogTag oldTag = blogTagMapper.selectByTagName(oldTagName);
        BlogTag newTag = blogTagMapper.selectByTagName(newTagName);
        
        if (oldTag != null && newTag == null){
            oldTag.setTagName(newTagName);
            blogTagMapper.updateByPrimaryKeySelective(oldTag);
        }
    
        return false;
    }
    
    @Override
    public PageProperties getTagListWithKeyword(PageUtil pageUtil) {
    
        int totalCount = blogTagMapper.getTotalTagCount(pageUtil);
        List<BlogTag> tagList = blogTagMapper.getTagList(pageUtil);
        PageProperties pageProperties = new PageProperties(totalCount,
                pageUtil.getPageVolume(),
                pageUtil.getCurrentPage(),
                tagList);
    
        return pageProperties;
    }
    
    
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        //删除tag存在对博客和tag关系表的处理 即删除对应关系
        if (ids.length > 0){
            // relationMapper.deleteByTagId(ids);
            return blogTagMapper.deleteBatch(ids) > 0;
        }
        return false;
    }
    
    @Override
    public int countTag(PageUtil pageUtil) {
        return blogTagMapper.getTotalTagCount(pageUtil);
    }
    
    @Override
    public List<BlogTagCount> getHotTagWithCount() {
        return blogTagMapper.getTagWithCount();
    }
}
