package com.lyh.AtonBlog.util;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * 分页工具类
 */
@Data
public class PageUtil extends LinkedHashMap<String,Object> {
    
    //当前页码
    private int currentPage;
    //页面大小
    private int pageVolume;
    
    public PageUtil(Map<String,Object> map){
        this.putAll(map);
        
        this.currentPage = Integer.parseInt(map.get("currentPage").toString());
        this.pageVolume = Integer.parseInt(map.get("pageVolume").toString());
        this.put("start",(currentPage-1)*pageVolume);
        this.put("currentPage",currentPage);
        this.put("pageVolume",pageVolume);
        this.put("keyword",map.get("keyword"));
        
    }
    
}
