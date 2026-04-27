package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.CourseEnrollDTO;
import com.example.UniversityManagementSystem.dto.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.RegisterCourseDTO;
import com.example.UniversityManagementSystem.dto.UpdateCourseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.enums.CourseStatus;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.repository.CourseRepository;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final StudentService studentService;
    private final StudentRepository studentRepository;

    public CourseService(CourseRepository courseRepository, StudentService studentService, StudentRepository studentRepository) {

        this.courseRepository = courseRepository;
        this.studentService = studentService;
        this.studentRepository = studentRepository;
    }

    // Create course method
    @PreAuthorize("hasRole('ROLE_LECTURER')") // Only users with the LECTURER role can create courses
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

    // Update course details method
    public void updateCourseDetails(UpdateCourseDTO request) {
        // Get the course by courseId
        Course crs = getCourseByCourseId(request.getCourseId());

        // Update the course details
        if (request.getCourseName() != null && request.getBadge() != null && request.getDurationDays()!= 0) {
            crs.setCourseName(request.getCourseName());
            crs.setBadge(request.getBadge());
            crs.setDurationDays(request.getDurationDays());
            log.info("Updating course with ID: {}", request.getCourseId());
            log.info("-- Course Name: {}", request.getCourseName());
            log.info("-- Badge: {}", request.getBadge());
            log.info("-- Duration: {}", request.getDurationDays());

        } else if (request.getCourseName() != null) {
            crs.setCourseName(request.getCourseName());
            log.info("Updating course with ID: {}", request.getCourseId());
            log.info("-- Course Name: {}", request.getCourseName());
        } else if (request.getBadge() != null) {
            crs.setBadge(request.getBadge());
            log.info("Updating course with ID: {}", request.getCourseId());
            log.info("-- Badge: {}", request.getBadge());
        } else if (request.getDurationDays() != 0) {
            crs.setDurationDays(request.getDurationDays());
            log.info("Updating course with ID: {}", request.getCourseId());
            log.info("-- Duration: {}", request.getDurationDays());
        }

        // Save the course in the database
        courseRepository.save(crs);
    }

    // Enroll students in a course method
    public void enrollStudentIntoCourse(CourseEnrollDTO request) {
        // Validate the courseId
        Course crs = getCourseByCourseId(request.getCourseId());

        // Validate the studentId
        Student std = studentService.findStudentByStudentId(request.getStudentId());

        // Check if the student is ACTIVE, if not throw an exception
        if (std.getStudentStatus() == StudentStatus.INACTIVE) {
            throw new RuntimeException("Student is not active and cannot be enrolled in a course");
        }

        // Check whether course status is SCHEDULED, if not throw an exception
        if (crs.getCourseStatus() != CourseStatus.SCHEDULED) {
            throw new RuntimeException("Course is not scheduled and cannot accept new enrollments");
        }

        // Check whether the student is already enrolled in other courses, as a student can only be enrolled in one course as of now
        if (std.getCourse() != null) {
            throw new RuntimeException("Student is already enrolled in a course and cannot be enrolled in another course");
        }


        // Then add the courseId to the student entity and save the student entity to the database
        std.setCourse(crs);
        studentRepository.save(std);

        // Increase the number of participants in the course table
        crs.setEnrolledStudentsCount(crs.getEnrolledStudentsCount() + 1);
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

