package com.lyh.AtonBlog.pojo;

import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import lombok.Data;

import javax.xml.ws.soap.Addressing;

/**
 * 
 * @TableName tb_config
 */
@Data
@ApiModel("设置模型")
public class Config implements Serializable {
    /**
     * 配置项的名称
     */
    private String configName;

    /**
     * 配置项的值
     */
    private String configValue;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 修改时间
     */
    private Date updateTime;

    private static final long serialVersionUID = 1L;
}