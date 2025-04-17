package com.nicepromax.common.annotation;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface IpLimiter {

    String ip();

    long limitCount() default 10;

    long time() default 1;

    String message();
}
