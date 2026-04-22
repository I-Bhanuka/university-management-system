package com.example.UniversityManagementSystem.dto;


import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateStudentDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;

    private String firstName;
    private String lastName;
    private String email;
    private LocalDate dob;
}
