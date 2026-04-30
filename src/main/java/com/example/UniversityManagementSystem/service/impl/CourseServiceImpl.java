package com.example.UniversityManagementSystem.service.impl;

import com.example.UniversityManagementSystem.dto.requestDTOs.CourseEnrollDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterCourseDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateCourseDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.CourseCount;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.enums.CourseStatus;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.exception.AlreadyEnrolledException;
import com.example.UniversityManagementSystem.exception.BadRequestException;
import com.example.UniversityManagementSystem.exception.CourseNotFoundException;
import com.example.UniversityManagementSystem.exception.NotFoundException;
import com.example.UniversityManagementSystem.repository.CourseRepository;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import com.example.UniversityManagementSystem.service.CourseService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {

    private final CourseRepository courseRepository;
    private final StudentServiceImpl studentService;
    private final StudentRepository studentRepository;

    // Create course method
    @PreAuthorize("hasRole('ADMIN')") // Only users with the LECTURER role can create courses
    @Override
    public void createCourse(RegisterCourseDTO request) {

        log.info("====== Starting course creation process ======");

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
        log.info("Trying to save course with ID: {}", request.getCourseId());
        courseRepository.save(course);
    }

    // Read all courses method
    // Anyone authenticated can view courses
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Override
    public Page<Course> getAllCourses(Pageable pageable) {

        log.info("==================== Get All Courses =================");

        Page<Course> result = courseRepository.findAll(pageable);

        if (result.isEmpty()) {
            log.info("No courses found in the database");
            throw new NotFoundException("No courses found in the database");
        }

        log.info("Total courses found: {}", result.getTotalElements());
        for (Course course : result) {
            log.info("Course ID: {}, Course Name: {}, Status: {}, Enrolled Students: {}",
                    course.getCourseId(), course.getCourseName(), course.getCourseStatus(), course.getEnrolledStudentsCount());
        }

        return result;
    }


    // Read a course by ID method
    // Anyone authenticated can view a course
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Override
    public Course getCourseById(CourseIdDTO request) {

        log.info("==================== Get Course By Course Id =================");

        Course crs = getCourseByCourseId(request.getCourseId());

        log.info("Course with ID {} found: {}", request.getCourseId(), crs);

        return crs;

    }

    // De-activating a course method
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deactivateCourseById(CourseIdDTO request) {

        log.info("==================== Deactivate Course =================");

        // Get the course by courseID
        Course crs = getCourseByCourseId(request.getCourseId());

        // Check whether the course is null
        if (crs == null) {
            log.info("Course with courseId {} not found to de-activate", request.getCourseId());
            throw new RuntimeException("Course not found");
        }

        // Set the status to COMPLETED
        log.info("Course with courseId {} found to de-activate", request.getCourseId());
        crs.setCourseStatus(CourseStatus.COMPLETED);

        // Save the course
        log.info("Trying to save course with ID: {} after de-activation", request.getCourseId());
        courseRepository.save(crs);


    }

    // Update course details method
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void updateCourseDetails(UpdateCourseDTO request) {

        log.info("==================== Update Course Details =================");
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
    @PreAuthorize("hasRole('ADMIN')")
    @Transactional
    @Override
    public void enrollStudentIntoCourse(CourseEnrollDTO request) {

        log.info("==================== Enroll Student Into Course =================");

        // Validate the courseId
        Course crs = getCourseByCourseId(request.getCourseId());

        // Validate the studentId
        Student std = studentService.findStudentByStudentId(request.getStudentId());

        // Check if the student is ACTIVE, if not throw an exception
        if (std.getStudentStatus() == StudentStatus.INACTIVE) {
            throw new BadRequestException("Student is not active and cannot be enrolled in a course");
        }

        // Check whether course status is SCHEDULED, if not throw an exception
        if (crs.getCourseStatus() != CourseStatus.SCHEDULED) {
            throw new BadRequestException("Course is not scheduled and cannot accept new enrollments");
        }

        // Check whether the student is already enrolled in other courses, as a student can only be enrolled in one course as of now
        if (std.getCourse() != null) {
            throw new AlreadyEnrolledException(std.getStudentId());
        }


        // Then add the courseId to the student entity and save the student entity to the database
        log.info("Enrolling student into course with ID: {}", request.getStudentId());
        std.setCourse(crs);
        studentRepository.save(std);

        // Increase the number of participants in the course table
        crs.setEnrolledStudentsCount(crs.getEnrolledStudentsCount() + 1);
        log.info("Student enrolled into course with ID: {}", request.getStudentId());
        courseRepository.save(crs);

    }

    @PreAuthorize("hasAnyRole('ADMIN')")
    @Override
    public List<CourseCount> countStudentsByCourse() {

        log.info("Count student by course");

        return courseRepository.countStudentsByCourse();
    }

    // Helper method to find the course by courseId
    public Course getCourseByCourseId(String courseId) {

        // Find the Course
        log.info("Get course by course ID: {}", courseId);
        Course crs = courseRepository.findByCourseId(courseId).orElse(null);

        // If course is not found, log the error and throw an exception
        if (crs == null){
            log.info("Course with courseId {} not found", courseId);
            throw new CourseNotFoundException("Course not found");
        }

        return crs;
    }
}

