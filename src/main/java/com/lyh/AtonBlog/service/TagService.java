package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.BlogTag;
import com.lyh.AtonBlog.pojo.BlogTagCount;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TagService {
    
    Boolean addTag(String tagName);
    
    Boolean updateTag(String oldTagName,String newTagName);
    
    PageProperties getTagListWithKeyword(PageUtil pageUtil);
    
    Boolean deleteBatch(Integer[] ids);
    
    int countTag(PageUtil pageUtil);
    
    //前台服务
    /**
     * 获取热门标签
     */
    List<BlogTagCount> getHotTagWithCount();
}
