package com.miniedu.course_service.cache.service.impl;

import com.miniedu.course_service.cache.service.ICacheLayerService;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryCachingService implements ICacheLayerService {

    private final Map<String, Object> cache = new ConcurrentHashMap<>();

    @Override
    public void put(String key, Object value) {
        cache.put(key, value);
    }

    @Override
    public Object get(String key) {
        return cache.get(key);
    }
}