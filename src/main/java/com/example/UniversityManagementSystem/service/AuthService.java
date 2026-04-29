package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.JwtResponseDTO;
import com.example.UniversityManagementSystem.dto.LoginUserRequestDTO;
import com.example.UniversityManagementSystem.dto.RegisterUserRequestDTO;

public interface AuthService {

    // Register a new user
    void register(RegisterUserRequestDTO request);

    // Login the user and return the JWT token
    JwtResponseDTO login(LoginUserRequestDTO request);

}
