package com.example.UniversityManagementSystem.security;

import com.example.UniversityManagementSystem.dto.ErrorResponseDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.core.AuthenticationException;

import java.io.IOException;

public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(
            HttpServletRequest request,
            HttpServletResponse response,
            AuthenticationException authException) throws IOException {

        response.setStatus(401);
        response.setContentType("application/json");

        ErrorResponseDTO error = ErrorResponseDTO.of(401,
                "Authentication required. Please login.");

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule()); // To handle LocalDateTime serialization
        mapper.writeValue(response.getOutputStream(), error);

    }
}
