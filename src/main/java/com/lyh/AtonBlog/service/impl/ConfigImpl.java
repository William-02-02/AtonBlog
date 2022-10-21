package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.ConfigMapper;
import com.lyh.AtonBlog.pojo.Config;
import com.lyh.AtonBlog.service.ConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class ConfigImpl implements ConfigService {
    
    
    public static final String websiteName = "personal blog";
    public static final String websiteDescription = "personal blog是SpringBoot2+Thymeleaf+Mybatis建造的个人博客网站.SpringBoot实战博客源码.个人博客搭建";
    public static final String websiteLogo = "/admin/dist/img/logo2.png";
    public static final String websiteIcon = "/admin/dist/img/favicon.png";
    
    public static final String yourAvatar = "/admin/dist/img/13.png";
    public static final String yourEmail = "1198640816@qq.com";
    public static final String yourName = "Aton";
    
    public static final String footerAbout = "your personal blog. have fun.";
    public static final String footerICP = "Null";
    public static final String footerCopyRight = "Null";
    public static final String footerPoweredBy = "personal blog";
    public static final String footerPoweredByURL = "##";
    
    @Autowired
    ConfigMapper configMapper;
    
    @Override
    public Boolean updateConfig(Map<String,String> map) {
        ArrayList<Config> configlist = new ArrayList<>();
    
        //这里我修改成用map存储所有修改的信息
        for (Map.Entry<String, String> entry : map.entrySet()) {
            //把每一条遍历出来 获得相应的config对象，修改属性 然后封装到configMap中
            String configName = entry.getKey();
            String configValue = entry.getValue();
            Config config = configMapper.selectByPrimaryKey(configName);
            if (config != null){
                config.setConfigValue(configValue);
                config.setUpdateTime(new Date());
                configlist.add(config);
            }
        }
    
        return configMapper.updateBatch(configlist) > 0;
    }
    
    @Override
    public Map<String,String> getAllConfig() {
        List<Config> configs = configMapper.selectAllConfig();
        //把所有的config项封装成map
        Map<String, String> configMap = configs.stream()
                .collect(Collectors.toMap(Config::getConfigName, Config::getConfigValue));
        
        //对map中的项进行判空 与赋默认值
        for (Map.Entry<String, String> config : configMap.entrySet()) {
            
            if ("websiteName".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(websiteName);
            }
            if ("websiteDescription".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(websiteDescription);
            }
            if ("websiteLogo".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(websiteLogo);
            }
            if ("websiteIcon".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(websiteIcon);
            }
            if ("yourAvatar".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(yourAvatar);
            }
            if ("yourEmail".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(yourEmail);
            }
            if ("yourName".equals(config.getKey()) && config.getValue().isEmpty()){
                config.setValue(yourName);
            }
            if ("footerAbout".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerAbout);
            }
            if ("footerICP".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerICP);
            }
            if ("footerCopyRight".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerCopyRight);
            }
            if ("footerPoweredBy".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredBy);
            }
            if ("footerPoweredByURL".equals(config.getKey()) && StringUtils.isEmpty(config.getValue())) {
                config.setValue(footerPoweredByURL);
            }
        }
        return configMap;
    }
}
