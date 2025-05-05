package com.nicepromax.redislingpaiannotation.handler;


import com.nicepromax.redislingpaiannotation.annotation.RequestLimiter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Aspect
@Component
public class RequestLimiterHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;

//    @PostConstruct
//    public void init(){
//        redisScript = new DefaultRedisScript<>();
//        redisScript.setResultType(Long.class);
//        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("requestLimiter.lua")));
//    }

    @Around("@annotation(requestLimiter)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, RequestLimiter requestLimiter) throws Throwable {
        // 获取当前请求路径
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        String url = request.getRequestURI();
        // 检查注解是否应用在方法上
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("改注解只能使用在方法上");
        }
        String msg = requestLimiter.msg();
        String name = ((MethodSignature) signature).getMethod().getName();
        System.out.println(name);
        // 获取令牌
        String token = stringRedisTemplate.opsForList().rightPop(url);
        if (token == null){
            return msg;
        }
        return proceedingJoinPoint.proceed();
    }
}
