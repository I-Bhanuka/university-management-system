package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.RegisterCourseDTO;
import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.ResponseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.service.CourseService;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private CourseService courseService;

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
    @GetMapping
    public List<Course> getAllCourses() {
        return courseService.getAllCourses();
    }

    // Read a course by ID
    @GetMapping("/getCourseById")
    public Course getCourseById(@Valid @RequestBody CourseIdDTO request) {
        return courseService.getCourseById(request);
    }

    // De-activating a course

}
