package com.miniedu.course_service.service.course;

import com.miniedu.course_service.dto.course.*;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.UUID;

@Service
public interface ICourseService {
    ReserveSeatResponse reserveSeat(ReserveSeatRequest request);
    CreateCourseResponse createCourse(CreateCourseRequest request);
    CourseView publishCourse(UUID courseId);
    CourseView archiveCourse(UUID courseId);
    CourseView unreserveSeat(UUID courseId);
    CourseView updateCourse(UUID courseId, Map<String, Object> fields);
}
