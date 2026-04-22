package com.example.UniversityManagementSystem.dto;

import com.example.UniversityManagementSystem.enums.CourseStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RegisterCourseDTO {

    @NotBlank(message = "Course ID is required")
    private String courseId;

    @NotBlank(message = "Course Name is required")
    private String courseName;

    @NotNull(message = "Duration in days is required")
    private int durationDays;

    @NotBlank(message = "Badge is required")
    private String badge;

    private CourseStatus courseStatus = CourseStatus.SCHEDULED;
}
