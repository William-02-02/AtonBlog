package com.lyh.AtonBlog.controller.admin;

import com.lyh.AtonBlog.config.Constants;
import com.lyh.AtonBlog.util.MyBlogUtils;
import com.lyh.AtonBlog.util.Result;
import com.lyh.AtonBlog.util.ResultGenerator;
import io.swagger.annotations.Api;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

@RequestMapping("/admin")
@Controller
@Api(tags = "封面上传")
public class UploadController {
    
    @PostMapping("/upload/file")
    @ResponseBody
    public Result upload(HttpServletRequest httpServletRequest, @RequestParam("file") MultipartFile file) throws URISyntaxException {
        String fileName = file.getOriginalFilename();
        //获取.后面的文件后缀
        String suffixName = fileName.substring(fileName.lastIndexOf("."));
        //生成文件名称通用方法
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
        Random r = new Random();
        StringBuilder tempName = new StringBuilder();
        //文件以日期+随机数+后缀的形式命名
        tempName.append(sdf.format(new Date())).append(r.nextInt(100)).append(suffixName);
        String newFileName = tempName.toString();
        //注意 这里new File只是创建了一个file对象 并没有写入到真实路径中
        File fileDirectory = new File(Constants.FILE_UPLOAD_DIC);
        //创建文件
        File destFile = new File(Constants.FILE_UPLOAD_DIC + newFileName);
        try {
            if (!fileDirectory.exists()) {
                //文件夹不存在就新建
                if (!fileDirectory.mkdirs()) {
                    throw new IOException("文件夹创建失败,路径为：" + fileDirectory);
                }
            }
            //把文件转移到目标处
            file.transferTo(destFile);
            Result resultSuccess = ResultGenerator.genSuccessResult();
            //把uri中的信息封装到响应中 用于前台显示上传的图片
            resultSuccess.setData(MyBlogUtils.getHost(new URI(httpServletRequest.getRequestURL() + "")) + "/upload/" + newFileName);
            return resultSuccess;
        } catch (IOException e) {
            e.printStackTrace();
            return ResultGenerator.genFailResult("文件上传失败");
        }
    }

    
}
