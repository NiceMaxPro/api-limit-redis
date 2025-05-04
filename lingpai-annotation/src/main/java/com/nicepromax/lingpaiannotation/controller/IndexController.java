package com.nicepromax.lingpaiannotation.controller;

import com.nicepromax.lingpaiannotation.annotation.RequestLimiter;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.TimeUnit;

@RestController
@RequestMapping("/index")
public class IndexController {

    @RequestLimiter(QPS = 1D, timeout = 200, timeunit = TimeUnit.MILLISECONDS,msg = "服务器繁忙,请稍后再试")
    @GetMapping("/test")
    @ResponseBody
    public String test(){
        return "11111";
    }
}
