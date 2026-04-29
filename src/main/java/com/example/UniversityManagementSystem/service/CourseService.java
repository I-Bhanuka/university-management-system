package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.CourseEnrollDTO;
import com.example.UniversityManagementSystem.dto.CourseIdDTO;
import com.example.UniversityManagementSystem.dto.RegisterCourseDTO;
import com.example.UniversityManagementSystem.dto.UpdateCourseDTO;
import com.example.UniversityManagementSystem.entity.Course;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;


public interface CourseService {

    // Create course method
    void createCourse(RegisterCourseDTO request);

    // Read all courses method
    List<Course> getAllCourses();

    // Read a course by ID method
    Course getCourseById(CourseIdDTO request);

    // De-activating a course method
    void deactivateCourseById(CourseIdDTO request);

    // Update course details method
    void updateCourseDetails(UpdateCourseDTO request);

    // Enroll students in a course method
    void enrollStudentIntoCourse(CourseEnrollDTO request);

    // Get all courses with number of students enrolled
    Object countStudentsByCourse();

}

