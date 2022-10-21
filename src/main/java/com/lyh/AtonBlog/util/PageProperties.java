package com.lyh.AtonBlog.util;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PageProperties {
    
    //总记录数
    private int totalCount;
    //每页记录条目数
    private int pageVolume;
    //总页数
    private int totalPageCount;
    //当前页数
    private int currentPage;
    //列表数据
    private List<?> data;
    
    public PageProperties(int totalCount, int pageVolume,  int currentPage, List<?> data) {
        this.totalCount = totalCount;
        this.pageVolume = pageVolume;
        this.totalPageCount = (int) Math.ceil((double) totalCount/pageVolume);
        this.currentPage = currentPage;
        this.data = data;
    }
}
