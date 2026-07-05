package com.miniedu.course_service.entities.catalog;

import com.miniedu.course_service.dto.catalog.CatalogView;
import com.miniedu.course_service.entities.course.Course;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity(name = "catalogs")
@Table(name = "catalogs",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_catalog_name", columnNames = "name")
        }
)
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Catalog {
    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true)
    private String name;

    @OneToMany(mappedBy = "catalog", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Course> courses = new HashSet<>();

    public void addCourse(Course course) {
        courses.add(course);
        course.setCatalog(this);
    }

    public CatalogView toView() {
        return new CatalogView(this.getId(), this.getName());
    }
}
