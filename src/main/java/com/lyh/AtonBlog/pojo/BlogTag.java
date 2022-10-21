package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_tag
 */
@Data
@ApiModel("标签模型")
public class BlogTag implements Serializable {
    /**
     * 标签表主键id
     */
    private Integer tagId;

    /**
     * 标签名称
     */
    private String tagName;

    /**
     * 是否删除 0=否 1=是
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}