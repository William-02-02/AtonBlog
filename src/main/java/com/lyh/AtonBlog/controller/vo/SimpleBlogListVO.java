package com.lyh.AtonBlog.controller.vo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("simple博客视图模型")
public class SimpleBlogListVO {
    private Long blogId;
    
    private String blogTitle;
}
