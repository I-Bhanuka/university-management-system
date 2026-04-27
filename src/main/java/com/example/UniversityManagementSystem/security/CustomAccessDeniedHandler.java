package com.example.UniversityManagementSystem.security;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class CustomAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request,
                       HttpServletResponse response,
                       AccessDeniedException accessDeniedException) throws IOException {

        String path = request.getRequestURI();

        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");

        Map<String, Object> body = new HashMap<>();
        body.put("status", 403);
        body.put("error", "Forbidden");

        // Custom message
        if (path.contains("/courses")) {
            body.put("message", "Only admins can manage courses");
        } else {
            body.put("message", "Access denied");
        }

        // Add extra info
        body.put("path", request.getRequestURI());
        body.put("timestamp", System.currentTimeMillis());



        new ObjectMapper().writeValue(response.getOutputStream(), body);
    }
}