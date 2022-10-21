package com.lyh.AtonBlog.service.impl;

import com.lyh.AtonBlog.controller.vo.BlogDetailVO;
import com.lyh.AtonBlog.controller.vo.BlogListVO;
import com.lyh.AtonBlog.controller.vo.SimpleBlogListVO;
import com.lyh.AtonBlog.dao.*;
import com.lyh.AtonBlog.pojo.Blog;
import com.lyh.AtonBlog.pojo.BlogCategory;
import com.lyh.AtonBlog.pojo.BlogTag;
import com.lyh.AtonBlog.pojo.BlogTagRelation;
import com.lyh.AtonBlog.service.BlogService;
import com.lyh.AtonBlog.util.MarkDownUtil;
import com.lyh.AtonBlog.util.PageProperties;
import com.lyh.AtonBlog.util.PageUtil;
import com.lyh.AtonBlog.util.PatternUtil;
import io.swagger.annotations.ApiModel;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureOrder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;
import org.thymeleaf.util.PatternUtils;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BlogServiceImpl implements BlogService {
    
    @Autowired
    BlogMapper blogMapper;
    
    @Autowired
    BlogCategoryMapper categoryMapper;
    
    @Autowired
    BlogTagMapper tagMapper;
    
    @Autowired
    BlogTagRelationMapper relationMapper;
    
    @Autowired
    BlogCommentMapper commentMapper;
    
    @Override
    @Transactional
    public String saveBlog(Blog blog) {
    
        //如果你没有指定分类 那就默认分类
        // if (blog.getBlogCategoryId() == null){
        //     blog.setBlogCategoryId(0);
        //     blog.setBlogCategoryName("默认分类");
        // }
        
        //分类处理
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory == null){
            //不存在该分类 那就新建 并且把category_rank +1 用于分类排序
            //上面的功能需要前端支持 我不太会~~~~  所以就用作者的 如果没有指定分类就用默认
            // blogCategory.setCategoryName(blog.getBlogCategoryName());
            // categoryMapper.insertSelective(blogCategory);
            
            blog.setBlogCategoryId(0);
            blog.setBlogCategoryName("默认分类");
        }else {
            //存在该分类 那就补充blog的信息
            blog.setBlogCategoryName(blogCategory.getCategoryName());
            blogCategory.setCategoryRank(blogCategory.getCategoryRank() + 1);
        }
    
        
        System.out.println("BlogService: tags:" + blog.getBlogTags());
        //处理标签
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6){
            return "标签数量限制为6";
        }
        
        //保存文章到blog中
        if (blogMapper.insertSelective(blog) > 0){
            //把需要新增的tag对象全部新增到tag表中
            //这里可以把tag进行一个分类 把需要新增的放一起
            ArrayList<BlogTag> tagListForInsert = new ArrayList<>();
    
            ArrayList<BlogTag> allTagList = new ArrayList<>();
            
            //遍历所有tag 把需要新增的装到list中
            for (int i = 0; i < tags.length; i++) {
                BlogTag temp = tagMapper.selectByTagName(tags[i]);
                if (temp == null){
                    //不存在这个tag 那就新增
                    //我这样写会需要多次调用sql 效率不是很高
                    //可以选择把这些都封装到tagListForInsert中
                    // tagMapper.insertByTagName(tags[i]); 
                    BlogTag blogTag = new BlogTag();
                    blogTag.setTagName(tags[i]);
                    tagListForInsert.add(blogTag);
                }else {
                    //已经存在的就直接放入
                    allTagList.add(temp);
                }
            }
            
            //分完类就把需要新增的添加进去
            if ( !CollectionUtils.isEmpty(tagListForInsert)){
                //如果有要插入的tag
                tagMapper.BatchInsertTagList(tagListForInsert);
            }
            if (blogCategory != null){
                //这个用来更新category的rank
                categoryMapper.updateByPrimaryKeySelective(blogCategory);
            }
            
            //建一个BlogTagRelation的List 用来存放所有tag 用于更新relation表
            List<BlogTagRelation> blogTagRelationList = new ArrayList<>();
            //前面只把不需要新增的tag放到了allTagList中 这里把需要新增的也复制过来
            allTagList.addAll(tagListForInsert);
            
            //遍历allTagList 处理BlogTageRelation对象
            for (BlogTag i : allTagList) {
                BlogTagRelation relation = new BlogTagRelation();
                relation.setBlogId(blog.getBlogId());
                relation.setTagId(i.getTagId());
                blogTagRelationList.add(relation);
                
            }
            
            if (relationMapper.insertBatch(blogTagRelationList) > 0){
                return "success";
            }
        }
    
            return "保存失败";
    
    
        // //检查是否已经存在
        // Blog temp = blogMapper.selectByBlogTitle(blog.getBlogTitle());
        // if (temp != null){
        //     return blogMapper.insertSelective(blog) > 0;
        // }
        //
        // int i = blogMapper.insertSelective(blog);
    
    }
    
    @Override
    @Transactional
    public String updateBlog(Blog blog) {
        //得到待更新的博客
        Blog blogForUpdate = blogMapper.selectByPrimaryKey(blog.getBlogId());
        if (blogForUpdate == null){
            return "数据不存在";
        }
        //把修改后的数据 填到要更新的对象中
        blogForUpdate.setBlogTitle(blog.getBlogTitle());
        blogForUpdate.setBlogSubUrl(blog.getBlogSubUrl());
        blogForUpdate.setBlogContent(blog.getBlogContent());
        blogForUpdate.setBlogCoverImage(blog.getBlogCoverImage());
        blogForUpdate.setBlogStatus(blog.getBlogStatus());
        blogForUpdate.setEnableComment(blog.getEnableComment());
    
        //对于需要更新的blog的分类进行处理 
        BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
        if (blogCategory==null){
            //分类不存在就设置成默认
            blogForUpdate.setBlogCategoryId(0);
            blogForUpdate.setBlogCategoryName("默认分类");
        }else {
            //分类存在就设置成该分类 并rank+1
            blogForUpdate.setBlogCategoryName(blog.getBlogCategoryName());
            blogForUpdate.setBlogCategoryId(blog.getBlogCategoryId());
            //rank+1
            blogCategory.setCategoryRank(blogCategory.getCategoryRank());
        }
        //处理标签数据
        String[] tags = blog.getBlogTags().split(",");
        if (tags.length > 6){
            return "标签数量限制为6";
        }
        blogForUpdate.setBlogTags(blog.getBlogTags());
        //新增的tag对象
        List<BlogTag> tagListForInsert = new ArrayList<>();
        //所有tag对象
        List<BlogTag> allTagList = new ArrayList<>();
        //遍历tag 对tag进行分类
        for (int i = 0; i < tags.length; i++) {
            BlogTag tag = tagMapper.selectByTagName(tags[i]);
            if (tag == null){
                //新增 
                BlogTag tempTag = new BlogTag();
                tempTag.setTagName(tags[i]);
                tagListForInsert.add(tempTag);
            }else {
                allTagList.add(tag);
            }
        }
        //把新tag正式写入数据库
        if ( !CollectionUtils.isEmpty(tagListForInsert)){
            tagMapper.BatchInsertTagList(tagListForInsert);
        }
        //分类表： 更新分类
        if (blogCategory != null){
            //这里主要是更新了 category表的rank
            categoryMapper.updateByPrimaryKeySelective(blogCategory);
        }
        
        //关系表： 删除原有关系 新增关系
        //新增关系数据
        List<BlogTagRelation> relationList = new ArrayList<>();
        allTagList.addAll(tagListForInsert);
        for (BlogTag tag : allTagList) {
            BlogTagRelation relation = new BlogTagRelation();
            relation.setTagId(tag.getTagId());
            relation.setBlogId(blog.getBlogId());
            relationList.add(relation);
        }
        
        relationMapper.deleteByPrimaryKey(blog.getBlogId());
        relationMapper.insertBatch(relationList);
        
        if (blogMapper.updateByPrimaryKeySelective(blogForUpdate) >0){
            return "success";
        }
        return "修改失败";
        
    }
    
    @Override
    @Transactional
    public Boolean deleteBatch(Integer[] ids) {
        
        //找出id对应的category
        List<Blog> blogs = blogMapper.selectAllByBlogIdBatch(ids);
        // for (Blog blog : blogs) {
        //     Integer Id = blog.getBlogCategoryId();
        //     BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(Id);
        //     blogCategory.setCategoryRank(blogCategory.getCategoryRank()-1);
        //     categoryMapper.updateByPrimaryKeySelective(blogCategory);
        // }
        
        //把id和需要减掉的rank存到map中
        HashMap<Integer, Integer> map = new HashMap<>();
        for (Blog blog : blogs) {
            Integer categoryId = blog.getBlogCategoryId();
            if (map.containsKey(categoryId)){
                map.replace(categoryId,map.get(categoryId)+1);
            }else {
                map.put(categoryId,1);
            }
        }
        categoryMapper.updateRankByCategoryId(map);
        
        
        //解除relation
        relationMapper.deleteBatchByBlogId(ids);
    
        return blogMapper.deleteBatch(ids) >0;
    }
    
    @Override
    public PageProperties getBlogByKeyword(PageUtil pageUtil) {
        int totalCount = blogMapper.countBlog(pageUtil);
        List<Blog> blogList = blogMapper.findBlogListByMap(pageUtil);
        PageProperties pageProperties = new PageProperties(totalCount,
                pageUtil.getPageVolume(),
                pageUtil.getCurrentPage(),
                blogList);
        return pageProperties;
    }
    
    @Override
    public int countBlog(PageUtil pageUtil) {
        return blogMapper.countBlog(pageUtil);
    }
    
    @Override
    public Blog getBlog(Long id) {
        return blogMapper.selectByPrimaryKey(id);
    }
    
    //==============前台方法
    @Override
    public PageProperties getBlogsForIndexPage(int page) {
    
        //封装查询条件到pageUtil
        HashMap hashMap = new HashMap();
        hashMap.put("currentPage",page);
        hashMap.put("pageVolume",8);
        hashMap.put("blogStatus",1);
        PageUtil pageUtil = new PageUtil(hashMap);
    
        //获取blogList 并封装到视图层对象
        int total = blogMapper.countBlog(pageUtil);
        List<Blog> blogList = blogMapper.findBlogListByMap(pageUtil);
        List<BlogListVO> blogListVO = getBlogListVOByBlogList(blogList);
        PageProperties pageProperties = new PageProperties(total, pageUtil.getPageVolume(), pageUtil.getCurrentPage(), blogListVO);
    
        return pageProperties;
    }
    
    
    
    /**
     * 封装blogList 到视图层对象 BlogListVO
     */
    public List<BlogListVO> getBlogListVOByBlogList(List<Blog> blogList){
        //将要返回的BlogListVO对象
        ArrayList<BlogListVO> blogListVOS = new ArrayList<>();
        //查询出来的BlogList如果不为空
        if (!CollectionUtils.isEmpty(blogList)){
            //如果bloglist不为空 就获得所有博客分类id
            List<Integer> categoryIdList = blogList.stream().map(Blog::getBlogCategoryId).collect(Collectors.toList());
    
            Map<Integer, String> categoryIdIconMap = new HashMap<>();
            if (!CollectionUtils.isEmpty(categoryIdList)){
                //分类id不为空 就根据id找到category对象
                List<BlogCategory> categoryList = categoryMapper.selectAllByCategoryIds(categoryIdList);
                //根据category对象list 获得id 和icon的映射map
                if (!CollectionUtils.isEmpty(categoryList)){
                    //这里是把分类id作为key 分类图标作为value 第三个是key冲突时的解决策略 选择y
                    categoryIdIconMap = categoryList.stream().collect(Collectors.toMap(BlogCategory::getCategoryId,
                            BlogCategory::getCategoryIcon, (x, y) -> y));
                }
            }
            //遍历blogList 为的是把blog对象封装成VO对象
            for (Blog blog : blogList) {
                BlogListVO blogListVO = new BlogListVO();
                //拷贝相同类型的字段到新对象中
                BeanUtils.copyProperties(blog,blogListVO);
                //如果id和icon的映射map中有blog中的categoryid 那就封装到VO中
                if (categoryIdIconMap.containsKey(blog.getBlogCategoryId())){
                    blogListVO.setBlogCategoryIcon(categoryIdIconMap.get(blog.getBlogCategoryId()));
                }else {
                    //如果map中不存在对应的categoryId 那就使用默认的分类
                    //这里我有点疑问，难道说默认分类不在数据库里吗？ 
                    blogListVO.setBlogCategoryId(0);
                    blogListVO.setBlogCategoryName("默认分类");
                    blogListVO.setBlogCategoryIcon("/admin/dist/img/category/00.png");
                }
                //添加到返回的 vo list中。
                blogListVOS.add(blogListVO);               
            }
        }
        return blogListVOS;
    }
    
    /**
     * 获取右侧 最多点击和最新发布博客 模型
     */
    @Override
    public List<SimpleBlogListVO> getSimpleBlogListForIndexPage(int type) {
        ArrayList<SimpleBlogListVO> simpleBlogListVOS = new ArrayList<>();
        //根据type 决定是根据时间排序 还是根据点击排序 整合在一个sql中实现
        List<Blog> blogs = blogMapper.selectBlogListByType(type, 9);
        //封装到SimpleBlogListVO中
        for (Blog blog : blogs) {
            SimpleBlogListVO simpleBlogListVO = new SimpleBlogListVO();
            BeanUtils.copyProperties(blog,simpleBlogListVO);
            simpleBlogListVOS.add(simpleBlogListVO);
        }
        return simpleBlogListVOS;
    }
    
    /**
     * 根据blog封装成blogDetailVO对象
     */
    @Override
    public BlogDetailVO getBlogDetailVO(Blog blog) {
        if (blog != null && blog.getBlogStatus()==1){
            //增加浏览量
            blog.setBlogViews(blog.getBlogViews() +1);
            blogMapper.updateByPrimaryKeySelective(blog);
            //封装成DetailVO对象
            BlogDetailVO blogDetailVO = new BlogDetailVO();
            BeanUtils.copyProperties(blog,blogDetailVO);
            //暂时缺少一个md转html 测试一下没有转是怎样的。
            blogDetailVO.setBlogContent(MarkDownUtil.mdToHtml(blogDetailVO.getBlogContent()));
            
            //处理分类
            BlogCategory blogCategory = categoryMapper.selectByPrimaryKey(blog.getBlogCategoryId());
            //如果没有对应分类 就附上默认 说明默认分类是没有在数据库中的
            //这里我决定不太合适 还不如把默认分类当作一个不能删除的数据库字段来的简单
            if (blogCategory ==null){
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
                blogCategory.setCategoryName("默认分类");
                blogCategory.setCategoryIcon("/admin/dist/img/category/00.png");
            }
            blogDetailVO.setBlogCategoryIcon(blogCategory.getCategoryIcon());
            //处理标签 由于Blog中标签以String存储 而DetailVo中以List<String>存在 所以要分割一下
            if (!blog.getBlogTags().isEmpty()){
                List<String> tags = Arrays.asList(blog.getBlogTags().split(","));
                blogDetailVO.setBlogTags(tags);
            }
            
            //获得评论数
            blogDetailVO.setCommentCount(commentMapper.countByBlogId(blog.getBlogId()));
            return blogDetailVO;
        }
        
        return null;
    }
    
    /**
     * 根据博客 id获得 blogDetailVO对象
     */
    @Override
    public BlogDetailVO getBlogDetailById(Long id) {
        //根据id获得blog对象
        Blog blog = blogMapper.selectByPrimaryKey(id);
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null){
            return blogDetailVO;
        }
        return null;
    }
    
    /**
     * 根据 subUrl 获得 blogDetailVO 对象
     */
    @Override
    public BlogDetailVO getBlogDetailBySubUrl(String subUrl) {
        //根据id获得blog对象
        Blog blog = blogMapper.selectByBlogSubUrl(subUrl);
        BlogDetailVO blogDetailVO = getBlogDetailVO(blog);
        if (blogDetailVO != null){
            return blogDetailVO;
        }
        return null;
    }
    
    
    /**
     * 根据tag筛选博客
     */
    @Override
    public PageProperties getBlogPageByTag(String tagName, int page) {
        BlogTag blogTag = tagMapper.selectByTagName(tagName);
        if (blogTag != null && page >0){
            Map map = new HashMap();
            map.put("currentPage",page);
            map.put("tagId",blogTag.getTagId());
            map.put("pageVolume",9);
            PageUtil pageUtil = new PageUtil(map);
            List<Blog> blogs = blogMapper.selectBlogPageByTagId(pageUtil);
            int total = blogMapper.getBlogCountByTagId(blogTag.getTagId());
            PageProperties pageProperties = new PageProperties(total, pageUtil.getPageVolume(), pageUtil.getCurrentPage(), blogs);
            return pageProperties;
        }
        return null;
    }
    
    /**
     * 根据分类筛选博客
     */
    @Override
    public PageProperties getBlogPageByCategory(String categoryName, int page) {
        if (PatternUtil.validKeyword(categoryName)) {
            //根据分类名找到分类对象
            BlogCategory blogCategory = categoryMapper.selectByCategoryName(categoryName);
            if ("默认分类".equals(categoryName) && blogCategory == null) {
                blogCategory = new BlogCategory();
                blogCategory.setCategoryId(0);
            }
            //根据分类对象找到符合条件的博客 并封装数据
            if (blogCategory != null && page > 0) {
                Map param = new HashMap();
                param.put("currentPage", page);
                param.put("pageVolume", 9);
                param.put("blogCategoryId", blogCategory.getCategoryId());
                param.put("blogStatus", 1);//过滤发布状态下的数据
                PageUtil pageUtil = new PageUtil(param);
                List<Blog> blogList = blogMapper.findBlogListByMap(pageUtil);
                List<BlogListVO> blogListVOS = getBlogListVOByBlogList(blogList);
                int total = blogMapper.getBlogCountByCategoryId(blogCategory.getCategoryId());
                PageProperties pageResult = new PageProperties(total, pageUtil.getPageVolume(), pageUtil.getCurrentPage(),blogListVOS);
                return pageResult;
            }
        }
        return null;
    }
    
    
    /**
     * 根据 keyword查找博客
     */
    @Override
    public PageProperties getBlogPageBySearch(String keyword, int page) {
        if (page >0 && PatternUtil.validKeyword(keyword)){
            Map map = new HashMap();
            map.put("currentPage",page);
            map.put("keyword",keyword);
            map.put("pageVolume",9);
            map.put("blogStatus",1);
            PageUtil pageUtil = new PageUtil(map);
            List<Blog> blogs = blogMapper.findBlogListByMap(pageUtil);
            List<BlogListVO> blogListVO = getBlogListVOByBlogList(blogs);
            int total = blogMapper.countBlog(pageUtil);
            PageProperties pageProperties = new PageProperties(total, pageUtil.getPageVolume(), pageUtil.getCurrentPage(), blogListVO);
            return pageProperties;
        }
        return null;
    }
}
