package com.miniedu.course_service.repository;

import com.miniedu.course_service.entities.catalog.Catalog;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CatalogRepository extends JpaRepository<Catalog, UUID> {

    @EntityGraph(attributePaths = {"courses"}, type = EntityGraph.EntityGraphType.FETCH)
    Catalog findCatalogById(@Param("catalogId") UUID catalogId);

    @EntityGraph(attributePaths = {"courses"}, type = EntityGraph.EntityGraphType.FETCH)
    @Query(value = "SELECT c FROM catalogs c")
    List<Catalog> findAllCatalogs();
}
