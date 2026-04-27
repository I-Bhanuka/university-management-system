package com.example.UniversityManagementSystem.repository;

import com.example.UniversityManagementSystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface CourseRepository extends JpaRepository<Course, Integer> {

    Optional<Course> findByCourseId(String courseId);


    // Custom JPQL query to find course they are enrolled in

    // Count students by the course they are enrolled in
    @Query("""
        SELECT c.courseName, COUNT(s)
        FROM Course c
        JOIN Student s ON s.course.id = c.id
        GROUP BY c.courseName
    """)
    List<Object[]> countStudentsByCourse();
}
