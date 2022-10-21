package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 
 * @TableName tb_blog
 */
@Data
@ApiModel("博客模型")
public class Blog implements Serializable {
    /**
     * 博客表主键
     */
    private Long blogId;

    /**
     * 博客标题
     */
    private String blogTitle;

    /**
     * 博客自定义路径url
     */
    private String blogSubUrl;

    /**
     * 博客封面图
     */
    private String blogCoverImage;

    /**
     * 博客内容
     */
    private String blogContent;

    /**
     * 博客分类id
     */
    private Integer blogCategoryId;

    /**
     * 博客分类（冗余）
     */
    private String blogCategoryName;

    /**
     * 博客标签
     */
    private String blogTags;

    /**
     * 0-草稿 1-发布
     */
    private Byte blogStatus;

    /**
     * 阅读量
     */
    private Long blogViews;

    /**
     * 0-允许评论 1-不允许评论
     */
    private Byte enableComment;

    /**
     * 是否删除 0-否 1-是
     */
    private Byte isDeleted;

    /**
     * 创建时间
     */
    // @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}