package com.miniedu.course_service.repository;

import com.miniedu.course_service.entities.course.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface CoursesRepository extends JpaRepository<Course, UUID> {

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
                UPDATE Course c 
                SET c.availableSeats = c.availableSeats - 1,
                    c.status = CASE WHEN c.availableSeats = 1 THEN 'CLOSED' ELSE c.status END
                WHERE c.id = :courseId 
                  AND c.status = 'PUBLISHED' 
                  AND c.availableSeats > 0
            """)
    int reserveSeat(@Param("courseId") UUID courseId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
                UPDATE Course c 
                SET c.availableSeats = c.availableSeats + 1,
                    c.status = CASE WHEN c.status = 'CLOSED' THEN 'PUBLISHED' ELSE c.status END
                WHERE c.id = :courseId 
                  AND c.availableSeats < c.totalSeats
            """)
    int unreserveSeat(@Param("courseId") UUID courseId);

    @Modifying(flushAutomatically = true, clearAutomatically = true)
    @Query("""
                UPDATE Course c
                SET c.status = 'ARCHIVED'
                WHERE c.id = :courseId
                  AND c.status != 'PUBLISHED'
            """)
    int archiveCourse(@Param("courseId") UUID courseId);
}

