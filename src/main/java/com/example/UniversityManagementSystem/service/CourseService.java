package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.RegisterCourseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.enums.CourseStatus;
import com.example.UniversityManagementSystem.repository.CourseRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    // Create course method
    public void createCourse(RegisterCourseDTO request) {

        // Create a new course entity from the request data
        Course course = Course.builder()
                .courseId(request.getCourseId())
                .courseName(request.getCourseName())
                .durationDays(request.getDurationDays())
                .badge(request.getBadge())
                .courseStatus(CourseStatus.SCHEDULED)
                .build();

        log.info("Creating course with ID: {}", request.getCourseId());
        log.info("Course Name: {}", request.getCourseName());
        log.info("Duration (days): {}", request.getDurationDays());
        log.info("Badge: {}", request.getBadge());
        log.info("Course Status: {}", request.getCourseStatus());

        // Create the course in the database
        courseRepository.save(course);
    }

    // Read all courses method
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
}
