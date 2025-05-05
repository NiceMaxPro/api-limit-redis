package com.nicepromax.redislingpaiannotation.controller;

import com.nicepromax.redislingpaiannotation.annotation.RequestLimiter;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api")
public class IndexController {

    @RequestLimiter
    @RequestMapping("/test")
    public String test(){
        return "test";
    }
}
