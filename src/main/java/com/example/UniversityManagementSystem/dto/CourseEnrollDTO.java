package com.example.UniversityManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseEnrollDTO {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Student ID is required")
    private  String studentId;
}
