package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.StudentResponseDTO;
import com.example.UniversityManagementSystem.entity.Student;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

public interface StudentService {

    Student getStudent(StudentIdDTO request);

    // Get all students without pagination
    List<Student> getAllStudents();

    // Get all sorted students without pagination
    List<Student> getAllStudentsSorted(Sort sort);

    // Get all students with pagination and sorting
    Page<Student> getAllStudentsPaginatedAndSorted(Pageable pageable);

    void registerStudent(RegisterStudentDTO request);

    void deactivateStudent(StudentIdDTO request);

    Student updateStudent(UpdateStudentDTO request);

    ArrayList<StudentResponseDTO> searchStudent(String firstName, String email);

}
