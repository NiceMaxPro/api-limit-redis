package com.nicepromax.common.handler;
import com.google.common.base.Preconditions;
import com.nicepromax.common.annotation.IpLimiter;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.Resource;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.script.DefaultRedisScript;
import org.springframework.scripting.support.ResourceScriptSource;
import org.springframework.stereotype.Component;
import java.util.Collections;

@Aspect
@Component
public class IpLimiterHandler {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private DefaultRedisScript<Long> redisScript;


    /**
     * 初始化lua脚本
     */
    @PostConstruct
    public void init(){
        redisScript = new DefaultRedisScript<>();
        redisScript.setResultType(Long.class);
        redisScript.setScriptSource(new ResourceScriptSource(new ClassPathResource("ipLimiter.lua")));
    }

    @Around("@annotation(ipLimiter)")
    public Object around(ProceedingJoinPoint proceedingJoinPoint, IpLimiter ipLimiter) throws Throwable {
        // 检查注解是否应用在方法上
        Signature signature = proceedingJoinPoint.getSignature();
        if (!(signature instanceof MethodSignature)) {
            throw new IllegalArgumentException("the Annotation @IpLimter must used on method!");
        }
        // 获取注解参数
        // 限流模块IP
        String limitIp = ipLimiter.ip();
        Preconditions.checkNotNull(limitIp);
        // 限流阈值
        long limitCount = ipLimiter.limitCount();
        // 限流超时时间
        long expireTime = ipLimiter.time();
        // 限流提示语
        String message = ipLimiter.message();
        // 执行lua脚本
        // 执行lua脚本
        Long result = stringRedisTemplate
                .execute(
                        redisScript,
                        Collections.singletonList(limitIp),
                        Long.toString(expireTime),
                        Long.toString(limitCount)
                );
        if (result == 0) {
            // 达到限流返回给前端信息
            return message;
        }
        return proceedingJoinPoint.proceed();
    }

}
