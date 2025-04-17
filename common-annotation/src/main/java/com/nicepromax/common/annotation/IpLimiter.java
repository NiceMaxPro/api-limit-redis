package com.nicepromax.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IpLimiter {

    String ip();

    long limitCount() default 10; //限制次数

    long time() default 1;//限制时间

    String message() default "请求失败,你的IP访问太频繁";
}
