package com.nicepromax.common.controller;

import com.nicepromax.common.annotation.IpLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {

    private static final String MESSAGE = "请求失败,你的IP访问太频繁";


    @GetMapping("/ipLimit")
    @IpLimiter(ip = "127.0.0.1",limitCount = 5,time = 10,message = MESSAGE)
    public String ipLimit(){
        return "请求成功";
    }
}
