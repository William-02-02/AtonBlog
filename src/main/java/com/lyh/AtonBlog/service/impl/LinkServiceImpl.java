package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.dao.LinkMapper;
import com.lyh.AtonBlog.pojo.Link;
import com.lyh.AtonBlog.service.LinkService;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import io.swagger.models.auth.In;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class LinkServiceImpl implements LinkService {
    
    @Autowired
    LinkMapper linkMapper;
    
    @Override
    public Boolean addLink(Link record) {
        //判空 判是否存在  不需要担心record会不会时空的 因为controller层帮我们判了。
        //这里最好用url来查 因为主键是自动赋予的 而不是前段指定的 所以record中的PK是Null。
        Link temp = linkMapper.selectByLinkUrl(record.getLinkUrl());
        if (temp == null){
            return linkMapper.insertSelective(record) > 0;
        }
        return false;
    }
    
    @Override
    public Boolean updateLink(Link record) {
        //这里为什么可以用PK呢 因为更新需要选择一向 所有前端传递过来的record有id
        Link oldLink = linkMapper.selectByPrimaryKey(record.getLinkId());
        //判断是否存在
        if (oldLink != null){
            return linkMapper.updateByPrimaryKeySelective(record) > 0;
        }
        return false;
    }
    
    @Override
    public Boolean deleteBatch(Integer[] ids) {
        //这里似乎不需要判断 ids长度 因为是Controller传过来的 在上一层已经判断过了
        return linkMapper.deleteBatch(ids) >0;
    }
    
    @Override
    public PageProperties findLinkListByKeyword(PageUtil pageUtil) {
    
        int totalCount = linkMapper.getLinkCount(pageUtil);
        List<Link> linkList = linkMapper.findLinkListByKeyword(pageUtil);
        PageProperties pageProperties = new PageProperties(totalCount,
                pageUtil.getPageVolume(),
                pageUtil.getCurrentPage(),
                linkList);
        return pageProperties;
    }
    
    @Override
    public Link linkDetail(int id) {
        Link link = linkMapper.selectByPrimaryKey(id);
        return link;
    }
    
    @Override
    public int countLink(PageUtil pageUtil) {
        return linkMapper.getLinkCount(pageUtil);
    }
    
    @Override
    public Map<Integer, List<Link>> getLinksForLinkPage() {
        List<Link> linkList = linkMapper.findLinkListByKeyword(null);
        //封装成map形式
        if (!CollectionUtils.isEmpty(linkList)){
            Map<Integer, List<Link>> linkMap = linkList.stream().collect(Collectors.groupingBy(Link::getLinkType));
            return linkMap;
        }
        return null;
    }
}
