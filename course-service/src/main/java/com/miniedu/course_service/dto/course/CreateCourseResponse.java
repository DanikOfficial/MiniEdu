package com.miniedu.course_service.dto.course;

import java.util.UUID;

public record CreateCourseResponse(
        UUID catalogId,
        String catalogName,
        CourseView courseData
) {}