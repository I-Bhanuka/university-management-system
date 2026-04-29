package com.example.UniversityManagementSystem.exception;

import com.example.UniversityManagementSystem.dto.responseDTOs.ErrorResponseDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.security.authorization.AuthorizationDeniedException;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // 1. Custom exception handler - Every custom exception will be handled by ApplicationException
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponseDTO> handleApplicationException(
            ApplicationException e) {

        log.error("Application Exception: [{}]: {}", e.getStatusCode(), e.getMessage());

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponseDTO.of(e.getStatusCode(), e.getMessage()));
    }

    // 2. Validation fails of @Valid annotated request bodies will be handled by MethodArgumentNotValidException
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidation(
            MethodArgumentNotValidException e) {

        Map<String, String> fieldErrors = new HashMap<>();

        // e.getBindingResult().getFieldErrors() will return a list of FieldError objects
        // Then the forEach loop will iterate over each FieldError object and put the field name and error message into the fieldErrors map
        e.getBindingResult().getFieldErrors()
                .forEach(err -> fieldErrors.put(err.getField(), err.getDefaultMessage()));

        log.error("Validation Exception: {}", fieldErrors);

        ErrorResponseDTO error = ErrorResponseDTO.builder()
                .status(400)
                .message("Validation failed")
                .details(fieldErrors)
                .build();

        return ResponseEntity
                .status(400)
                .body(error);
    }

    // 3. Handle malformed JSON request body
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorResponseDTO> handleMalformedJson(
            HttpMessageNotReadableException e) {

        log.error("Malformed JSON request: {}", e.getMessage());

        return ResponseEntity
                .status(400)
                .body(ErrorResponseDTO.of(400, "Malformed JSON request"));
    }

    // 4. Handle Bad credentials for authentication
    // When authenticationManager.authenticate() fails (wrong password)
    // This IS reachable from AuthService which is called from AuthController
    // The BadCredentialsException is thrown automatically by Spring Security when authentication fails due to invalid credentials.
    public ResponseEntity<ErrorResponseDTO> handleBadCredentials(
            BadCredentialsException e) {

        log.warn("Bad credentials: {}", e.getMessage());

        return ResponseEntity
                .status(401)
                .body(ErrorResponseDTO.of(401, "Invalid username or password"));
    }

    // 5. Handle Access Denied for authorization failures
    // Handles @PreAuthorize failures — method level authorization
    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ErrorResponseDTO> handleAuthorizationDenied(
            AuthorizationDeniedException e) {

        log.warn("Authorization denied: {}", e.getMessage());

        return ResponseEntity
                .status(403)
                .body(ErrorResponseDTO.of(403, "Access denied. You do not have permission to perform this action."));
    }


    // 6. SAFETY NET
    //  Catches anything we didn't anticipate — always last
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleGeneric(Exception e) {

        log.error("Unexpected error: {}", e.getMessage(), e);

        return ResponseEntity
                .status(500)
                .body(ErrorResponseDTO.of(500, "An unexpected error occurred"));
    }


}
