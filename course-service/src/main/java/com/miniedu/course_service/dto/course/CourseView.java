package com.miniedu.course_service.dto.course;

import com.miniedu.course_service.entities.course.enums.CourseStatus;

import java.util.UUID;

public record CourseView(
        UUID id,
        String author,
        String title,
        String description,
        CourseStatus status,
        int availableSeats
) {}