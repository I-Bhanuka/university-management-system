package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.requestDTOs.CourseEnrollDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterCourseDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateCourseDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.CourseCount;
import com.example.UniversityManagementSystem.dto.responseDTOs.ResponseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.service.impl.CourseServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor
public class CourseController {

    private final CourseServiceImpl courseService; // Dependency injection will be done by RequiredArgsConstructor

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

    // Custom endpoint to get the count of students enrolled in each course
    @GetMapping("/course-count")
    public ResponseEntity<List<CourseCount>> getCourseCounts() {

        List<CourseCount> response = courseService.countStudentsByCourse();

        return ResponseEntity.ok(response);
    }

    // Assign lecturers to a course

}
