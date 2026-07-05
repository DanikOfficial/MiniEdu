package com.miniedu.course_service.entities.course;

import com.miniedu.course_service.dto.course.CourseView;
import com.miniedu.course_service.entities.catalog.Catalog;
import com.miniedu.course_service.entities.course.enums.CourseStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "courses")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {
    @Id
    @GeneratedValue
    private UUID id;
    // For simplicity’s sake we won't create an entity for this

    @Column(nullable = false)
    private String author;

    // Two courses can have different authors but with the same name
    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private int availableSeats;

    @Column(nullable = false)
    private int totalSeats;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "catalog_id", nullable = false)
    private Catalog catalog;

    @Version // Protects against race conditions during manual updates
    private Long version;

    public CourseView toView() {
        return new CourseView(
                this.getId(),
                this.getAuthor(),
                this.getTitle(),
                this.getDescription(),
                this.getStatus(),
                this.getAvailableSeats()
        );
    }
}
