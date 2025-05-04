package com.nicepromax.lingpaiannotation.config;

import com.nicepromax.lingpaiannotation.Interceptor.RequestLimiterInterceptor;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private RequestLimiterInterceptor requestLimiterInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 注册请求限流拦截器
        registry.addInterceptor(requestLimiterInterceptor).addPathPatterns("/**");
    }
}
