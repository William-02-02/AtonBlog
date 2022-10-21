package com.lyh.AtonBlog.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("博客视图模型")
public class BlogListVO {
    
    private long blogId;
    
    private String blogTitle;
    
    private String blogSubUrl;
    
    private String blogCoverImage;
    
    private Integer blogCategoryId;
    private String blogCategoryIcon;
    private String blogCategoryName;
    
    private Date createTime;
}
