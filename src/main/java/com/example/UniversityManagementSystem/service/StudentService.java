package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.requestDTOs.NameEmailStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.entity.Student;

import java.util.List;

public interface StudentService {

    Student getStudent(StudentIdDTO request);

    List<Student> getAllStudents();

    void registerStudent(RegisterStudentDTO request);

    void deactivateStudent(StudentIdDTO request);

    Student updateStudent(UpdateStudentDTO request);

    Student searchStudent(NameEmailStudentDTO request);

}
