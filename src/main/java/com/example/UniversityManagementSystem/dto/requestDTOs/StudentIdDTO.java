package com.example.UniversityManagementSystem.dto.requestDTOs;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class StudentIdDTO {

    @NotBlank(message = "Student ID is required")
    private String studentId;
}
