package com.miniedu.course_service.config.entity;

import com.miniedu.course_service.config.entity.common.Create;
import com.miniedu.course_service.config.entity.common.Update;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;

@ConfigurationProperties(prefix = "course.fields")
@Getter
@Setter
public class CourseConfiguration {
    // Maps to valid-fields
    private List<String> validFields;

    // Maps to the 'rules' object
    private Rules rules;

    @Getter
    @Setter
    public static class Rules {
        private Update update;
        private Create create;
    }
}