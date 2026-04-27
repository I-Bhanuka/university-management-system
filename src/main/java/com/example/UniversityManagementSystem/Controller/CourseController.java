package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.*;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.service.CourseService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "bearerAuth")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Create a course endpoint
    @PostMapping
    public ResponseEntity<ResponseDTO> createCourse(@Valid @RequestBody RegisterCourseDTO request) {

        courseService.createCourse(request);

        return ResponseEntity.ok(new ResponseDTO("Course created Successfully"));
    }


    // Read all courses
    @PostMapping("/all")
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Read a course by ID
    @PostMapping("/getCourseById")
    public Course getCourseById(@Valid @RequestBody CourseIdDTO request) {
        return courseService.getCourseById(request);
    }

    // De-activating a course
    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDTO> deactivateCourseById(@Valid @RequestBody CourseIdDTO request) {
        courseService.deactivateCourseById(request);

        return ResponseEntity.ok(new ResponseDTO("Course deactivated successfully"));
    }

    // Update course details
    @PatchMapping("/update")
    public ResponseEntity<ResponseDTO> updateCourseDetails(@Valid @RequestBody UpdateCourseDTO request) {
        courseService.updateCourseDetails(request);

        return ResponseEntity.ok(new ResponseDTO("Course updated successfully"));
    }

    // Enroll students in a course
    @PostMapping("/enroll")
    public ResponseEntity<ResponseDTO> enrollStudentIntoCourse(@Valid @RequestBody CourseEnrollDTO request) {
        courseService.enrollStudentIntoCourse(request);

        return ResponseEntity.ok(new ResponseDTO("Student enrolled in course successfully"));
    }

    // Assign lecturers to a course

}
