package com.miniedu.course_service.config.entity;

import com.miniedu.course_service.config.entity.common.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "catalog.fields.rules")
@Getter
@Setter
public class CatalogConfiguration {
    private Update update;
}
