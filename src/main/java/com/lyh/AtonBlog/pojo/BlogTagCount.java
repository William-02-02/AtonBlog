package com.lyh.AtonBlog.pojo;

import io.swagger.annotations.ApiModel;
import lombok.Data;

@Data
@ApiModel("标签数量")
public class BlogTagCount {
    
    private int tagId;
    
    private String tagName;
    
    private int tagCount;
    
}
