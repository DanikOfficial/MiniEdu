package com.miniedu.course_service.cache.service;

public interface ICacheLayerService {
    void put(String key, Object value);
    Object get(String key);
}