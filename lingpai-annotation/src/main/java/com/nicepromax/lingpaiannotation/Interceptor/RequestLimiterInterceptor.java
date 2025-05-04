package com.nicepromax.lingpaiannotation.Interceptor;

import com.google.common.util.concurrent.RateLimiter;
import com.nicepromax.lingpaiannotation.annotation.RequestLimiter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class RequestLimiterInterceptor implements HandlerInterceptor {

    /**
     * 不同方法存放不同的令牌桶
     */
    private final Map<String, RateLimiter> rateLimiterMap = new ConcurrentHashMap<>();


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        if (handler instanceof HandlerMethod){
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            RequestLimiter rateLimit = handlerMethod.getMethodAnnotation(RequestLimiter.class);
            if (rateLimit != null){
                // 获取请求
                String url = request.getRequestURI();
                RateLimiter rateLimiter;
                if (!rateLimiterMap.containsKey(url)){
                    rateLimiter = RateLimiter.create(rateLimit.QPS());
                    // 每一个Url都创建一个令牌桶
                    rateLimiterMap.put(url, rateLimiter);
                }else {
                    rateLimiter = rateLimiterMap.get(url);
                }
                // 获取令牌
                boolean acquire = rateLimiter.tryAcquire(rateLimit.timeout(), rateLimit.timeunit());
                if (acquire){
                    return HandlerInterceptor.super.preHandle(request, response, handler);
                }else {
                    System.out.println(rateLimit.msg());
//                    response.setStatus(400);
                    response.setContentType("application/json;charset=UTF-8");
                    response.getWriter().write(rateLimit.msg());
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}