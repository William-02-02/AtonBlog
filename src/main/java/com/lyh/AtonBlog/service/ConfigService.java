package com.lyh.AtonBlog.service;

import com.lyh.AtonBlog.pojo.Config;

import java.util.List;
import java.util.Map;

public interface ConfigService {
    
    Boolean updateConfig(Map<String,String> map);
    
    Map<String,String> getAllConfig();
    
    
}
