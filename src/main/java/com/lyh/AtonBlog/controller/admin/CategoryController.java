package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.service.CategoryService;
import com.lyh.AtonBlog.util.PageUtil;
import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;


import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@Controller
@RequestMapping("/admin")
@Api(tags = "分类管理")
public class CategoryController {
    
    @Autowired
    private CategoryService categoryService;
    
    @GetMapping("/categories")
    @ApiOperation("跳转分类管理页面")
    public String categoryPage(HttpServletRequest request){
        request.setAttribute("path","categories");
        return "admin/category";
    }
    
    
    /**
     * 分类列表数据获取
     */
    @RequestMapping(value = "/categories/list", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation("获取分类")
    public Result getCategoryList(@RequestParam Map<String,Object> params){
        //前端填写的参数如果为空 则返还异常
        if (StringUtils.isEmpty(params.get("currentPage")) || StringUtils.isEmpty(params.get("pageVolume"))){
            return ResultGenerator.genFailResult("参数异常");
        }
        PageUtil pageUtil = new PageUtil(params);
        //调用service层获得需要显示的列表
        return ResultGenerator.genSuccessResult(categoryService.getCategoryList(pageUtil));
    }
    
    
    /**
     * 分类添加
     */
    @RequestMapping(value = "/categories/save",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("分类保存")
    public Result save(@RequestParam("categoryName")String categoryName,
                       @RequestParam("categoryIcon")String categoryIcon){
        //使用Result传递信息
        //对数据进行判空
        if(categoryName.isEmpty()){
            return ResultGenerator.genFailResult("分类名不能为空");
        }
        if (categoryIcon.isEmpty()){
            return ResultGenerator.genFailResult("分类图标不能为空");
        }
        if (categoryService.addCategory(categoryName,categoryIcon)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("分组已存在");
        }
        
    }
    
    /**
     * 分类更新
     */
    @RequestMapping(value = "/categories/update",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("分类更新")
    public Result update(@RequestParam("categoryId")Integer categoryId,
                         @RequestParam("categoryName")String categoryName,
                         @RequestParam("categoryIcon")String categoryIcon){
    
        //对数据进行判空
        if(categoryName.isEmpty()){
            return ResultGenerator.genFailResult("分类名不能为空");
        }
        if (categoryIcon.isEmpty()){
            return ResultGenerator.genFailResult("分类图标不能为空");
        }
        if (categoryService.updateCategory(categoryId,categoryName,categoryIcon)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("分类已存在");
        }
        
    }
    
    /**
     * 分类删除
     */
    @RequestMapping(value = "/categories/delete",method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation("分类删除")
    public Result delete(@RequestBody Integer[] ids){
        if (ids.length<1){
            return ResultGenerator.genFailResult("id不能为空");
        }
        if (categoryService.deleteCategories(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
        
        
    }
    
}
