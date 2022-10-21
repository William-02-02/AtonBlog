package com.lyh.AtonBlog.controller;

import com.lyh.AtonBlog.dao.BlogMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class updBlogCategoryTest {
    
    @Autowired
    BlogMapper blogMapper;
    
    @Test
    public void updBlogCatgory(){
        blogMapper.updateBlogCategories("test",3,new Integer[]{3});
    }
    
}
