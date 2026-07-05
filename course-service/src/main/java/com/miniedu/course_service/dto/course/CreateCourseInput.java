package com.miniedu.course_service.dto.course;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record CreateCourseInput(
        @NotBlank(message = "Author is mandatory") String author,
        @NotBlank(message = "Title is mandatory") String title,
        @NotBlank(message = "Description is mandatory") String description,
        @Min(value = 10, message = "Course needs to have at least 10 availableSeats") int availableSeats
) {}