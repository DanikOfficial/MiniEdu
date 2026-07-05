package com.miniedu.course_service.service.catalog;

import com.miniedu.course_service.dto.catalog.CatalogWithCoursesView;
import com.miniedu.course_service.dto.catalog.CatalogView;
import com.miniedu.course_service.dto.catalog.CreateCatalogRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface ICatalogService {
    CatalogView createCatalog(CreateCatalogRequest catalogRequest);
    List<CatalogView> listCatalogs();
    CatalogWithCoursesView getCatalogCourses(UUID catalogId);
}
