package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_tag_relation
 */
@Data
@ApiModel("关系模型")
public class BlogTagRelation implements Serializable {
    /**
     * 关系表id
     */
    private Long relationId;

    /**
     * 博客id
     */
    private Long blogId;

    /**
     * 标签id
     */
    private Integer tagId;

    /**
     * 添加时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}