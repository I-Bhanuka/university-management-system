package com.example.UniversityManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UpdateCourseDTO {
    @NotBlank(message = "Course ID is required")
    private String courseId;

    private String courseName;
    private String badge;
    private int durationDays;
}
