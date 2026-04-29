package com.example.UniversityManagementSystem.service.impl;

import com.example.UniversityManagementSystem.dto.responseDTOs.JwtResponseDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.LoginUserRequestDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterUserRequestDTO;
import com.example.UniversityManagementSystem.entity.User;
import com.example.UniversityManagementSystem.repository.UserRepository;
import com.example.UniversityManagementSystem.security.JwtUtil;
import com.example.UniversityManagementSystem.service.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {


    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    // Register a new user
    @Override
    public void register(RegisterUserRequestDTO request) {

        // Instantiate a new User entity and set its properties
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        // Save the new user to the database using the UserRepository
        userRepository.save(user);
        log.info("User registered: {}", request.getUsername());
    }


    // Login the user and return the JWT token
    @Override
    public JwtResponseDTO login(LoginUserRequestDTO request) {

        log.info("=== LOGIN ATTEMPT ===");
        log.info("Username: {}", request.getUsername());
        log.info("Password: {}", request.getPassword());

        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(),
                            request.getPassword()
                    )
            );

            log.info("=== AUTHENTICATION SUCCESS ===");

            // Generate JWT token using the authenticated user's details
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String token = jwtUtil.generateToken(userDetails);

            log.info("=== TOKEN GENERATED ===");

            return new JwtResponseDTO(token, userDetails.getUsername(), userDetails.getAuthorities().toString());

        } catch (Exception e) {
            log.error("=== AUTHENTICATION FAILED ===");
            log.error("Reason: {}", e.getMessage());
            log.error("Type: {}", e.getClass().getName());
            throw e;
        }
    }
}
