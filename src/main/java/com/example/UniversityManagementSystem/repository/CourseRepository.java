package com.example.UniversityManagementSystem.repository;

import com.example.UniversityManagementSystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CourseRepository extends JpaRepository<Course, Integer> {
}
