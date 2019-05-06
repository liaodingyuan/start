package com.liaody.sty.controller;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController

public class DockerLoginController {

    /**
     * 模拟docker login流程第一步
     * @param request 请求
     * @param response 响应
     */
    @GetMapping("/v2")
    public void v2(HttpServletRequest request, HttpServletResponse response) throws UnsupportedEncodingException {
        String userAuth = request.getHeader("Authorization");
        // 头部没有认证信息
        if(StringUtils.isEmpty(userAuth)){

        }
        // 头部已经带了认证信息
        else{
            // 获取头部，使用base64解码
            userAuth = userAuth.replaceAll("Bearer","").replaceAll(" ","");
            Base64.Decoder decoder = Base64.getDecoder();
            String userNamePawd =  new String (decoder.decode(userAuth), StandardCharsets.UTF_8);
            String username = userNamePawd.split(":")[0];
            String password = userNamePawd.split(";")[1];
            // 向Artifactory提交认证。
        }

    }
}
