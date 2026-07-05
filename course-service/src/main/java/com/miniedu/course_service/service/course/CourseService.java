package com.miniedu.course_service.service.course;

import com.miniedu.course_service.config.entity.CourseConfiguration;
import com.miniedu.course_service.config.entity.common.Update;
import com.miniedu.course_service.dto.course.*;
import com.miniedu.course_service.entities.catalog.Catalog;
import com.miniedu.course_service.entities.course.Course;
import com.miniedu.course_service.entities.course.enums.CourseStatus;
import com.miniedu.course_service.exception.BusinessLogicException;
import com.miniedu.course_service.exception.NotFoundException;
import com.miniedu.course_service.exception.UnavailableSeatException;
import com.miniedu.course_service.repository.CatalogRepository;
import com.miniedu.course_service.repository.CoursesRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@AllArgsConstructor
public class CourseService implements ICourseService {

    private final CoursesRepository coursesRepository;
    private final CatalogRepository catalogRepository;
    private final CourseConfiguration courseConfiguration;

    @Transactional
    @Override
    public ReserveSeatResponse reserveSeat(ReserveSeatRequest request) {
        int success = coursesRepository.reserveSeat(request.courseId());

        if (success == 0) {
            // Investigate if it's really missing or just full
            if (!coursesRepository.existsById(request.courseId())) {
                throw new NotFoundException("Course not found!");
            }
            throw new UnavailableSeatException("Error trying to enroll to a course, no available seats!");
        }

        return new ReserveSeatResponse(request.courseId());
    }

    @Transactional
    @Override
    public CreateCourseResponse createCourse(CreateCourseRequest request) {
        Catalog catalog = catalogRepository.findById(request.catalogId())
                .orElseThrow(() -> new NotFoundException("Catalog not found!"));

        CreateCourseInput input = request.courseData();
        Course newCourse = Course.builder()
                .author(input.author())
                .title(input.title())
                .description(input.description())
                .availableSeats(input.availableSeats())
                .totalSeats(input.availableSeats())
                .status(CourseStatus.DRAFT)
                .build();

        catalog.addCourse(newCourse);
        catalogRepository.save(catalog);

        return new CreateCourseResponse(
                catalog.getId(),
                catalog.getName(),
                newCourse.toView()
        );
    }

    @Transactional
    @Override
    public CourseView publishCourse(UUID courseId) {
        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));

        course.setStatus(CourseStatus.PUBLISHED);
        return course.toView();
    }

    @Transactional
    @Override
    public CourseView archiveCourse(UUID courseId) {
        int result = coursesRepository.archiveCourse(courseId);

        if (result == 0) {
            if (!coursesRepository.existsById(courseId)) {
                throw new NotFoundException("Course not found!");
            }
            throw new BusinessLogicException("You can only archive this course if its not published!");
        }

        return getCourseView(courseId);
    }

    @Transactional
    @Override
    public CourseView unreserveSeat(UUID courseId) {
        int result = coursesRepository.unreserveSeat(courseId);

        if (result == 0) {
            if (!coursesRepository.existsById(courseId)) {
                throw new NotFoundException("Course not found!");
            }
            throw new BusinessLogicException("Error while trying to unreserve seat!");
        }

        return getCourseView(courseId);
    }

    @Transactional
    @Override
    public CourseView updateCourse(UUID courseId, Map<String, Object> fields) {
        try {
            Course course = coursesRepository.findById(courseId)
                    .orElseThrow(() -> new NotFoundException("Course not found"));

            applyPatch(course, fields);

            return coursesRepository.save(course).toView();

        } catch (ObjectOptimisticLockingFailureException e) {
            throw new BusinessLogicException(
                    "The course has been modified by another user. Please refresh and try your update again."
            );
        }
    }

    private void applyPatch(Course course, Map<String, Object> updates) {
        List<String> validFields = courseConfiguration.getValidFields();
        Update updateRules = courseConfiguration.getRules().getUpdate();

        updates.forEach((key, value) -> {
            // 1. Validation: Does the field exist in our Domain definition?
            if (!validFields.contains(key)) {
                throw new BusinessLogicException("Field '" + key + "' is not a valid course field.");
            }

            // 2. Security: Is this field allowed to be modified via PATCH?
            if (updateRules.getBlockedFields().contains(key) || !updateRules.getAllowedFields().contains(key)) {
                throw new BusinessLogicException("Field '" + key + "' cannot be updated.");
            }

            // 3. Reflection: Dynamically update the entity
            Field field = ReflectionUtils.findField(Course.class, key);
            if (field != null) {
                ReflectionUtils.makeAccessible(field);

                // Optional: Type handling (ReflectionUtils.setField works best with correct types)
                // If inputs are always Strings from JSON, consider using a conversion service here.
                ReflectionUtils.setField(field, course, value);
            }
        });
    }

    private CourseView getCourseView(UUID courseId) {
        Course course = coursesRepository.findById(courseId)
                .orElseThrow(() -> new NotFoundException("Course not found"));
        return course.toView();
    }


}