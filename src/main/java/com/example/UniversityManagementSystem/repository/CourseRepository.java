package com.example.UniversityManagementSystem.repository;

import com.example.UniversityManagementSystem.dto.responseDTOs.CourseCount;
import com.example.UniversityManagementSystem.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCourseId(String courseId);


    // Custom JPQL query to find course they are enrolled in

    // Count students by the course they are enrolled in
    @Query("""
        SELECT new com.example.UniversityManagementSystem.dto.responseDTOs.CourseCount(c.courseName, COUNT(s) as studentCount)
        FROM Course c
        JOIN Student s ON s.course.id = c.id
        GROUP BY c.courseName
    """)
    List<CourseCount> countStudentsByCourse();

    Page<Course> findAll(Pageable pageable);
}
