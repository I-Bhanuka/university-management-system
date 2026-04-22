package com.example.UniversityManagementSystem.dto;

import lombok.*;
import jakarta.validation.constraints.NotBlank;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegisterStudentDTO {

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotBlank(message = "Date of Birth is required")
    private String dob;

    @NotBlank(message = "Email is required")
    private String email;
}
