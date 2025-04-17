package com.nicepromax.common.controller;

import com.nicepromax.common.annotation.IpLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class IpController {
    @GetMapping("/ipLimit")
    @IpLimiter(ip = "127.0.0.1",limitCount = 5,time = 10)
    public String ipLimit(){
        return "请求成功";
    }
}
