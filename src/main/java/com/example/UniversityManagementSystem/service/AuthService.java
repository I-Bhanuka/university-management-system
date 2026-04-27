package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.Controller.AuthController;
import com.example.UniversityManagementSystem.dto.JwtResponseDTO;
import com.example.UniversityManagementSystem.dto.LoginUserRequestDTO;
import com.example.UniversityManagementSystem.dto.RegisterUserRequestDTO;
import com.example.UniversityManagementSystem.entity.User;
import com.example.UniversityManagementSystem.enums.Role;
import com.example.UniversityManagementSystem.repository.UserRepository;
import com.example.UniversityManagementSystem.security.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(AuthenticationManager authenticationManager,
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    // Register a new user
    public void register(RegisterUserRequestDTO request) {

        // Instantiate a new User entity and set its properties
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.ROLE_ADMIN)
                .build();

        // Save the new user to the database using the UserRepository
        userRepository.save(user);
        log.info("User registered: {}", request.getUsername());
    }

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
