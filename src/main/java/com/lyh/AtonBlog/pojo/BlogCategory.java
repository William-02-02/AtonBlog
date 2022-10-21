package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @TableName tb_blog_category
 */
@Data
@ApiModel("分类模型")
public class BlogCategory implements Serializable {
    /**
     * 分类id
     */
    private Integer categoryId;

    /**
     * 分类名称
     */
    private String categoryName;

    /**
     * 分类图标
     */
    private String categoryIcon;

    /**
     * 分类被使用的越多rank越高数字越大
     */
    private Integer categoryRank;

    /**
     * 0-否 1-是
     */
    private Byte isDeleted;

    /**
     * 被创建的时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}