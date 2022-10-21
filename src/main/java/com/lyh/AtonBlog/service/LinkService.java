package com.lyh.AtonBlog.service;


import com.lyh.AtonBlog.pojo.Link;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface LinkService {
    
    Boolean addLink(Link record);
    
    Boolean updateLink(Link record);
    
    Boolean deleteBatch(Integer[] ids);
    
    PageProperties findLinkListByKeyword(PageUtil pageUtil);
    
    //得到某条link的详情
    Link linkDetail(int id);
    
    //获得link条数
    int countLink(PageUtil pageUtil);
    
    /**
     * 前台服务
     */
    Map<Integer,List<Link>> getLinksForLinkPage();
}
