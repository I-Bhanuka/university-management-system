package com.example.UniversityManagementSystem.dto.responseDTOs;

import com.example.UniversityManagementSystem.entity.Course;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentResponseDTO {
    private String firstName;
    private  String lastName;
    private String email;
    private LocalDate dob;
    private LocalDate enrollmentDate;
    private StudentStatus studentStatus;
    private Course course;

}
