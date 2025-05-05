package com.nicepromax.redislingpaiannotation.task;

import jakarta.annotation.Resource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@EnableScheduling // 开启定时任务
@EnableAsync // 开启多线程
public class MultithreadScheduleTask {

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    // 设置最大令牌数量
    @Value("${task.size:1000}")
    private long taskSize;

    @Async
    @Scheduled(fixedRateString ="${task.time:1000}")
    public void taskToken() {
        // 此处可以用lua脚本优化，但是问题不大？
        Long size = stringRedisTemplate.opsForList().size("/v1/api/test");
        if (size >= taskSize){
            return;
        }
        stringRedisTemplate.opsForList().leftPush("/v1/api/test", UUID.randomUUID().toString());
    }

}
