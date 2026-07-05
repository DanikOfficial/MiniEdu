package com.miniedu.course_service.dto.catalog;

import com.miniedu.course_service.dto.course.CourseView;

import java.util.List;
import java.util.UUID;

public record CatalogWithCoursesView(UUID catalogId, String catalogName, List<CourseView> courses) {
}
