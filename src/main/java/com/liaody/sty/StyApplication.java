package com.liaody.sty;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication
//@PropertySource("application.properties")
public class StyApplication {
    public static void main(String[] args) {
        SpringApplication.run(StyApplication.class, args);
    }

}
