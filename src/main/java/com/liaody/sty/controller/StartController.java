package com.liaody.sty.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sty/")
public class StartController {

    @GetMapping("/echo")
    @ApiOperation(value = "测试服务是否启动成功", notes = "测试")
    public String echo() {
        return "Sty Server Start Success!";
    }
}
