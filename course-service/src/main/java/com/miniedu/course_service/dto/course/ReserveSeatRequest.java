package com.miniedu.course_service.dto.course;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record ReserveSeatRequest(@NotBlank(message = "Course ID is mandatory") UUID courseId) {
}
