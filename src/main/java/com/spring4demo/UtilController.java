package com.spring4demo;

import com.google.code.kaptcha.Constants;
import com.google.code.kaptcha.Producer;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;

@Controller
public class UtilController {
    @Autowired
    private Producer verifyCodeProducer;

    @RequestMapping(value = "/hello2", method = RequestMethod.GET)
    public ModelAndView helloWorld2() {
        ModelAndView modelAndView = new ModelAndView("hello2");
        modelAndView.addObject("message", "Hello, World2!");
        return modelAndView;
    }
    

    @RequestMapping(value = "/getVerifyCodeImage", method = RequestMethod.GET)
    public void getVerifyCodeImage(HttpServletRequest request, HttpServletResponse response) {
        response.setContentType("image/jpeg"); // 設定為回傳一個 jpg 檔案
        String capText = verifyCodeProducer.createText(); // 建立驗證碼文字
        BufferedImage bi = verifyCodeProducer.createImage(capText) ;// 使用驗證碼文字建立驗證碼圖片

        HttpSession session = request.getSession();
        session.setAttribute(Constants.KAPTCHA_SESSION_KEY, capText);

        ServletOutputStream out = null;
        try {
            out = response.getOutputStream(); // 取得 ServletOutputStream 實例
            ImageIO.write(bi, "jpg", out); // 輸出圖片
            out.flush();  // 強制請求清空緩存區
        } catch (Exception e) {
            System.out.println(e);
        } finally {
            IOUtils.closeQuietly(out);
        }
    }
}