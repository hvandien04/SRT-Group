package com.example.todolist.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@RequiredArgsConstructor
@Component
public class CacheService {
    private final StringRedisTemplate redis;

    public boolean isCacheExist(String key) {
        return redis.hasKey(key);
    }

    public void deleteCache(String key) {
        redis.delete(key);
    }

    public void createCache(String key, String value, long expiration) {
        redis.opsForValue().set(key, value, expiration, TimeUnit.MINUTES);
    }

    public void setWithSeconds(String key, String value, long expiration) {
        redis.opsForValue().set(key, value, expiration, TimeUnit.SECONDS);
    }

    public int getIntegerValue(String key) {
        String value = redis.opsForValue().get(key);
        try {
            return (value == null) ? 0 : Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public void increment(String key, long amount, long ttlMinutes) {
        Long currentVal = redis.opsForValue().increment(key, amount);
        if (currentVal != null && currentVal == amount) {
            redis.expire(key, ttlMinutes, TimeUnit.MINUTES);
        }
    }
}
