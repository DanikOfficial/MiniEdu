package com.miniedu.course_service.cache.service.impl;

import com.miniedu.course_service.cache.service.ICacheLayerService;

public class NoOpCacheService implements ICacheLayerService {
    @Override
    public void put(String key, Object value) {
        // Do nothing
    }

    @Override
    public Object get(String key) {
        return null;
    }
}
