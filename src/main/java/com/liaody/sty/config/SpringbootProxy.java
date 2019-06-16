package com.liaody.sty.config;

import com.google.common.collect.ImmutableMap;
import org.mitre.dsmiley.httpproxy.ProxyServlet;
import org.springframework.boot.web.servlet.ServletRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.servlet.Servlet;
import java.util.Map;

@Configuration
public class SpringbootProxy {
//    @Bean
//    public Servlet baiduProxyServlet(){
//        return new ProxyServlet();
//    }

//    @Bean
//    public ServletRegistrationBean proxyServletRegistration(){
//        ServletRegistrationBean registrationBean = new ServletRegistrationBean(baiduProxyServlet(), "/*");
//        Map<String, String> params = ImmutableMap.of(
//                "targetUri", "http://www.what21.com",
//                "log", "true");
//        registrationBean.setInitParameters(params);
//        return registrationBean;
//    }
}
