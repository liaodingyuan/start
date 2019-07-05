package com.liaody.sty;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
@MapperScan("com.liaody.sty.dao")
public class StyApplication {

    public static void main(String[] args) {
//        System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(StyApplication.class, args);
    }

}
