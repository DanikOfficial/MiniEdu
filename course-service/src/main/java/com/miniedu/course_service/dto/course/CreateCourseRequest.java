package com.miniedu.course_service.dto.course;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public record CreateCourseRequest(@NotBlank(message = "Catalog Id is mandatory") UUID catalogId,
                                  @Valid CreateCourseInput courseData) {
}

