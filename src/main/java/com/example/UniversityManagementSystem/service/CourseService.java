package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.RegisterCourseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.entity.Student;
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

    // Read a course by ID method
    public Course getCourseById(CourseIdDTO request) {
        Course crs = getCourseByCourseId(request.getCourseId());

        log.info("Course with ID {} found: {}", request.getCourseId(), crs);

        return crs;

    }

    // De-activating a course method
    public void deactivateCourseById(CourseIdDTO request) {
        // Get the course by courseID
        Course crs = courseRepository.findByCourseId(request.getCourseId()).orElse(null);

        // Check whether the course is null
        if (crs == null) {
            log.info("Course with courseId {} not found to de-activate", request.getCourseId());
            throw new RuntimeException("Course not found");
        }

        // Set the status to COMPLETED
        crs.setCourseStatus(CourseStatus.COMPLETED);

        // Save the course
        courseRepository.save(crs);


    }

    // Helper method to find the course by courseId
    public Course getCourseByCourseId(String courseId) {
        // Find the Course
        Course crs = courseRepository.findByCourseId(courseId).orElse(null);

        // If course is not found, log the error and throw an exception
        if (crs == null){
            log.info("Course with courseId {} not found", courseId);
            throw new RuntimeException("Course not found");
        }

        return crs;
    }
}

