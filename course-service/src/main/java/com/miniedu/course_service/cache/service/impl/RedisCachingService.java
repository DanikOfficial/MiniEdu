package com.miniedu.course_service.cache.service.impl;

import com.miniedu.course_service.cache.config.CacheProperties;
import com.miniedu.course_service.cache.service.ICacheLayerService;
import lombok.AllArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;

@AllArgsConstructor
public class RedisCachingService implements ICacheLayerService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final CacheProperties cacheProperties;

    @Override
    public void put(String key, Object value) {
        // Use the configured value
        Duration ttl = Duration.ofMinutes(cacheProperties.getTtlMinutes());
        redisTemplate.opsForValue().set(key, value, ttl);
    }

    @Override
    public Object get(String key) {
        // Retrieves the object from Redis
        return redisTemplate.opsForValue().get(key);
    }
}