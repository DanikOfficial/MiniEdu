package com.miniedu.course_service.config.entity.common;

import lombok.Getter;
import lombok.Setter;
import java.util.Set;

@Getter
@Setter
public class Create {
    private Set<String> requiredFields;
}