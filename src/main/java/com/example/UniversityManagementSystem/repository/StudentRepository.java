package com.example.UniversityManagementSystem.repository;

import com.example.UniversityManagementSystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface StudentRepository extends JpaRepository<Student, UUID> {


    /**
     * Query,
     * SELECT *
     * FROM student
     * ORDER BY created_at DESC
     * LIMIT 1;
     */
    Optional<Student> findTopByOrderByCreatedAtDesc();

    /**
     * Query,
     * SELECT *
     * FROM student
     * WHERE studentid = ?;
     */
    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findTopByFirstName(String firstName);

    Optional<Student> findByEmail(String email);

    Optional<Student> findByFirstNameAndEmail(String studentId, String email);
}
