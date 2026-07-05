package com.miniedu.course_service.entities.course.enums;

public enum CourseStatus {
    DRAFT,      // Hidden from users
    PUBLISHED,  // Visible and accepting enrollments
    SUSPENDED,  // Temporarily paused by admin
    ARCHIVED ,   // Finished/Removed from active view
    CLOSED // Course has no available seats anymore
}