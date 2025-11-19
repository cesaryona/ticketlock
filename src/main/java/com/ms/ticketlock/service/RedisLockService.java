package com.ms.ticketlock.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RedisLockService {

    private final StringRedisTemplate redisTemplate;

    private static final Long LOCK_EXPIRE_SECONDS = 10L;

    public boolean acquireLock(String key) {
        var result = redisTemplate.opsForValue()
                .setIfAbsent(key, String.valueOf(LOCK_EXPIRE_SECONDS));

        return Boolean.TRUE.equals(result);
    }

    public void releaseLock(String key) {
        redisTemplate.delete(key);
    }
}
