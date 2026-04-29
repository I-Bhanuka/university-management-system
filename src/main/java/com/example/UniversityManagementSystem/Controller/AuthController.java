package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.responseDTOs.JwtResponseDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.LoginUserRequestDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterUserRequestDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.ResponseDTO;
import com.example.UniversityManagementSystem.service.impl.AuthServiceImpl;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthServiceImpl authService; // Dependency injection will be done by RequiredArgsConstructor

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@Valid @RequestBody RegisterUserRequestDTO request) {
        authService.register(request);

        return ResponseEntity.ok(new ResponseDTO("User registered successfully"));
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDTO> login(@Valid @RequestBody LoginUserRequestDTO request) {
        JwtResponseDTO response = authService.login(request);

        return  ResponseEntity.ok(response);


    }

}
