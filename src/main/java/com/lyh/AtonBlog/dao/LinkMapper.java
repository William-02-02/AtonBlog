package com.lyh.AtonBlog.dao;
import java.util.List;

import com.lyh.AtonBlog.util.PageUtil;
import org.apache.ibatis.annotations.Param;

import com.lyh.AtonBlog.pojo.Link;
import org.springframework.stereotype.Repository;

/**
 * @Entity com.lyh.AtonBlog.pojo.Link
 */
@Repository
public interface LinkMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Link record);

    int insertSelective(Link record);

    Link selectByPrimaryKey(Integer id);
    
    Link selectByLinkUrl(@Param("linkUrl") String linkUrl);

    int updateByPrimaryKeySelective(Link record);

    int updateByPrimaryKey(Link record);

    int deleteBatch(Integer[] ids);
    
    List<Link> findLinkListByKeyword(PageUtil pageUtil);
    
    int getLinkCount(PageUtil pageUtil);

}
