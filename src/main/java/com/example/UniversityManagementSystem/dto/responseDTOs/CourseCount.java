package com.example.UniversityManagementSystem.dto.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

@Getter
@Service
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CourseCount {

    private String courseName;
    private Long studentCount; // COUNT() always returns a Long, even if the count is zero
}
