package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

/**
 * 
 * @TableName tb_link
 */
@Data
@ApiModel("友链模型")
public class Link implements Serializable {
    /**
     * 友链表主键id
     */
    private Integer linkId;

    /**
     * 友链类别 0-友链 1-推荐 2-个人网站
     */
    private Integer linkType;

    /**
     * 网站名称
     */
    private String linkName;

    /**
     * 网站链接
     */
    private String linkUrl;

    /**
     * 网站描述
     */
    private String linkDescription;

    /**
     * 用于列表排序
     */
    private Integer linkRank;

    /**
     * 是否删除 0-未删除 1-已删除
     */
    private Byte isDeleted;

    /**
     * 添加时间
     */
    private Date createTime;

    private static final long serialVersionUID = 1L;
}