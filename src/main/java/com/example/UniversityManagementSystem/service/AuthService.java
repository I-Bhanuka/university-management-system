package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.responseDTOs.JwtResponseDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.LoginUserRequestDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterUserRequestDTO;

public interface AuthService {

    // Register a new user
    void register(RegisterUserRequestDTO request);

    // Login the user and return the JWT token
    JwtResponseDTO login(LoginUserRequestDTO request);

}
