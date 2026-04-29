package com.example.UniversityManagementSystem.dto.responseDTOs;

import lombok.AllArgsConstructor;
import lombok.Data;


@Data
@AllArgsConstructor
public class JwtResponseDTO {
    private String token;
    private String username;
    private String authorities;
}