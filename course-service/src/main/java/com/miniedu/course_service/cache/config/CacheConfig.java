package com.miniedu.course_service.cache.config;

import com.miniedu.course_service.cache.service.ICacheLayerService;
import com.miniedu.course_service.cache.service.impl.InMemoryCachingService;
import com.miniedu.course_service.cache.service.impl.NoOpCacheService;
import com.miniedu.course_service.cache.service.impl.RedisCachingService;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.redis.core.RedisTemplate;

@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

    @Bean
    @ConditionalOnProperty(prefix = "catalog.cache", name = "enabled", havingValue = "false")
    public ICacheLayerService noOpCacheService() {
        return new NoOpCacheService();
    }

    @Bean
    @Profile("redis")
    @ConditionalOnProperty(prefix = "catalog.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ICacheLayerService redisCache(RedisTemplate<String, Object> redisTemplate,
                                         CacheProperties cacheProperties) {
        return new RedisCachingService(redisTemplate, cacheProperties);
    }

    @Bean
    @Profile("!redis")
    @ConditionalOnProperty(prefix = "catalog.cache", name = "enabled", havingValue = "true", matchIfMissing = true)
    public ICacheLayerService inMemoryCache() {
        return new InMemoryCachingService();
    }
}