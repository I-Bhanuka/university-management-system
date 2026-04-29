package com.example.UniversityManagementSystem.dto.requestDTOs;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NameEmailStudentDTO {
    private String firstName;
    private String email;
}
