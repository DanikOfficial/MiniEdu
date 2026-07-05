package com.miniedu.course_service.cache.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "catalog.cache")
@Data // Use Lombok
public class CacheProperties {
    private boolean enabled = true;
    private long ttlMinutes = 30; // Default to 30
}