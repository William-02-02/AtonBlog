package com.lyh.AtonBlog.dao;

import com.lyh.AtonBlog.pojo.Config;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

/**
 * @Entity com.lyh.AtonBlog.pojo.Config
 */
@Repository
public interface ConfigMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Config record);

    int insertSelective(Config record);

    Config selectByPrimaryKey(String configName);

    int updateByPrimaryKeySelective(Config record);

    int updateByPrimaryKey(Config record);
    
    int updateBatch(List<Config> configList);

    int deleteBatch(Integer[] ids);
    
    List<Config> selectAllConfig();

}
