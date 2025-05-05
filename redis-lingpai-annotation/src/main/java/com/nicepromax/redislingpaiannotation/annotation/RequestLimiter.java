package com.nicepromax.redislingpaiannotation.annotation;

import java.lang.annotation.*;
import java.util.concurrent.TimeUnit;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface RequestLimiter {
    /**
      * 每秒创建令牌个数，默认:10
      */
    long QPS() default 10;

     /*** 获取令牌等待超时时间 默认:500
      */
     long timeout() default 500;

     /**
      * 超时时间单位 默认:毫秒
      */
     TimeUnit timeunit() default TimeUnit.MILLISECONDS;

     /**
      * 无法获取令牌返回提示信息
      */
     String msg() default "亲，服务器快被挤爆了，请稍后再试！";
}
