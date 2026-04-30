package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.StudentResponseDTO;
import com.example.UniversityManagementSystem.entity.Student;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

public interface StudentService {

    Student getStudent(StudentIdDTO request);

    Page<Student> getAllStudents(Pageable pageable);

    void registerStudent(RegisterStudentDTO request);

    void deactivateStudent(StudentIdDTO request);

    Student updateStudent(UpdateStudentDTO request);

    ArrayList<StudentResponseDTO> searchStudent(String firstName, String email);

}
