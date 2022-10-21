package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.service.CommentService;
import com.lyh.AtonBlog.util.PageProperties;
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
@Api(tags = "评论管理")
public class CommentController {
    
    @Autowired
    CommentService commentService;
    
    /**
     * 跳转评论页面
     */
    @GetMapping("/comments")
    @ApiOperation("跳转评论管理页面")
    public String commets(HttpServletRequest request){
        request.setAttribute("path","comments");
        return "admin/comment";
    }
    
    /**
     * 批量审核
     */
    @PostMapping("/comments/checkDone")
    @ResponseBody
    @ApiOperation("批准评论")
    public Result check(@RequestBody Long[] ids){
        //判空
        if (ids.length<0){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentService.checkComment(1,ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("审核失败");
        }
    }
    
    /**
     * 撤审 或未过审
     */
    @PostMapping("/comments/unCheck")
    @ResponseBody
    @ApiOperation("撤审评论")
    public Result unCheck(@RequestBody Long[] ids){
        //判空
        if (ids.length< 0){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentService.checkComment(-1,ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("撤销审核失败");
        }
    }
    
    /**
     * 回复
     */
    @PostMapping("/comments/reply")
    @ResponseBody
    @ApiOperation("回复评论")
    public Result reply(@RequestParam("commentId")Long commentId,
                        @RequestParam("replyBody")String replyBody){
        //判空 id不能为空 或负数
        if (commentId == null || commentId < 1 || replyBody.isEmpty()){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentService.replyComment(commentId, replyBody)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("回复失败");
        }
    }
    
    /**
     * 显示评论 可关键词
     */
    @GetMapping("/comments/list")
    @ResponseBody
    @ApiOperation("评论展示")
    public Result list(@RequestParam Map<String,Object> map){
    
        //如果不存在页码信息 就报错
        if (StringUtils.isEmpty(map.get("currentPage")) || StringUtils.isEmpty(map.get("pageVolume"))){
            return ResultGenerator.genFailResult("参数异常");       
        }
        PageUtil pageUtil = new PageUtil(map);
        PageProperties commentList = commentService.getCommentListByKeyword(pageUtil);
        return ResultGenerator.genSuccessResult(commentList);
    
    }
    
    /**
     * 删除
     */
    @PostMapping("/comments/delete")
    @ResponseBody
    @ApiOperation("评论删除")
    public Result delete(@RequestBody Integer[] ids){
        //判空
        if (ids.length < 1){
            return ResultGenerator.genFailResult("参数异常");
        }
        if (commentService.deleteBatch(ids)){
            return ResultGenerator.genSuccessResult();
        }else {
            return ResultGenerator.genFailResult("删除失败");
        }
    }

    
    
}
