package com.lyh.AtonBlog.controller.common;


import com.google.code.kaptcha.Producer;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Date;

@Controller
@Api(tags = "Kaptcha验证码")
public class CommonController {
    
    //Kaptcha
    @Autowired
    private Producer kaptchaProducer;
    
    @RequestMapping("/common/kaptcha")
    public void kaptchaProduce(HttpServletRequest req, HttpServletResponse resp){
    
    
        /**
         * 设置一些响应头
         */
        // Set to expire far in the past.
        resp.setDateHeader("Expires", 0);
        // Set standard HTTP/1.1 no-cache headers.
        resp.setHeader("Cache-Control", "no-store, no-cache, must-revalidate");
        // Set IE extended HTTP/1.1 no-cache headers (use addHeader).
        resp.addHeader("Cache-Control", "post-check=0, pre-check=0");
        // Set standard HTTP/1.0 no-cache header.
        resp.setHeader("Pragma", "no-cache");
        // return a jpeg
        resp.setContentType("image/jpeg");
    
        /**
         * 输出并存放信息 
         */
        //把当前时间存放到session中 用于检验是否超时
        req.getSession().setAttribute("verifyCodeGenTime",new Date());
        
        //生成文本
        String capText = kaptchaProducer.createText();
        //把文本存入session
        req.getSession().setAttribute("verifyCode",capText);
        
        //生成图片
        BufferedImage image = kaptchaProducer.createImage(capText);
        //使用输出流输出图片
        try {
            ServletOutputStream outputStream = resp.getOutputStream();
            ImageIO.write(image,"jpg",outputStream);
            outputStream.flush();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("验证码输出出错");
            return;
        }
        
    }
    
}
