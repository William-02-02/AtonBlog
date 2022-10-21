package com.lyh.AtonBlog.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Data
@ApiModel("Detail视图模型")
public class BlogDetailVO {
    /**
     * 博客自身属性
     */
    private Long blogId;
    
    private String blogTitle;
    
    private String blogCoverImage;
    
    private String blogContent;
    
    
    /**
     * 状态
     */
    private Long blogViews;
    
    private Byte enableComment;
    
    private Date createTime;
    
    private Integer commentCount;
    
    /**
     * 分类
     */
    private Integer blogCategoryId;
    
    private String blogCategoryName;
    
    private String blogCategoryIcon;
    
    /**
     * tag
     */
    private List<String> blogTags;
}
