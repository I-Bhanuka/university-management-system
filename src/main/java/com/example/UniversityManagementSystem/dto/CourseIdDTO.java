package com.example.UniversityManagementSystem.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseIdDTO {

    @NotBlank(message = "Course ID is required")
    private String courseId;
}
