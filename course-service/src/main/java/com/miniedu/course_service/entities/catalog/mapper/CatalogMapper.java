package com.miniedu.course_service.entities.catalog.mapper;

import com.miniedu.course_service.dto.catalog.CatalogWithCoursesView;
import com.miniedu.course_service.dto.course.CourseView;
import com.miniedu.course_service.entities.catalog.Catalog;
import com.miniedu.course_service.entities.course.Course;

import java.util.List;

public class CatalogMapper {
    public static CatalogWithCoursesView toDetailedView(Catalog catalog) {
        List<CourseView> courseViews = catalog.getCourses().stream()
                .map(Course::toView)
                .toList();

        return new CatalogWithCoursesView(
                catalog.getId(),
                catalog.getName(),
                courseViews
        );
    }
}