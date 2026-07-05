package com.miniedu.course_service.dto.catalog;

import jakarta.validation.constraints.NotBlank;

public record CreateCatalogRequest(@NotBlank(message = "The Catalog name is mandatory!") String name) {
}
