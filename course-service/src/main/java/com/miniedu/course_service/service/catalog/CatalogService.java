package com.miniedu.course_service.service.catalog;

import com.miniedu.course_service.dto.catalog.CatalogView;
import com.miniedu.course_service.dto.catalog.CatalogWithCoursesView;
import com.miniedu.course_service.dto.catalog.CreateCatalogRequest;
import com.miniedu.course_service.entities.catalog.Catalog;
import com.miniedu.course_service.exception.CatalogAlreadyExistsException;
import com.miniedu.course_service.exception.UnexpectedErrorException;
import com.miniedu.course_service.repository.CatalogRepository;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

import static com.miniedu.course_service.config.constants.BaseConstants.CATALOG_ALREADY_EXISTS_ERROR;
import static com.miniedu.course_service.config.constants.BaseConstants.ERROR_UNEXPECTED_ERROR;

@Service
@AllArgsConstructor
public class CatalogService implements ICatalogService {

    private final CatalogRepository catalogRepository;

    @Override
    public CatalogView createCatalog(CreateCatalogRequest catalogRequest) {
        Catalog newCatalog = Catalog.builder()
                .name(catalogRequest.name())
                .build();

        Catalog registeredCatalog;

        try {
          registeredCatalog =  catalogRepository.save(newCatalog);
        } catch (DataIntegrityViolationException ex) {
            var message = ex.getMessage();

            if (message.contains("uk_catalog_name")) {
                throw new CatalogAlreadyExistsException(CATALOG_ALREADY_EXISTS_ERROR);

            } else {
                throw new UnexpectedErrorException(ERROR_UNEXPECTED_ERROR);
            }
        }

        return registeredCatalog.toView();
    }

    // Catalogs don't need pagination, because this is not data heavy operation
    @Override
    public List<CatalogView> listCatalogs() {
        return catalogRepository.findAll().stream().map(Catalog::toView).toList();
    }

    @Override
    public CatalogWithCoursesView getCatalogCourses(UUID catalogId) {
        return null;
    }
}
