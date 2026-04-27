package com.example.UniversityManagementSystem.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL) // Omit null fields from JSON response
public class ErrorResponseDTO {

    private int status;
    private String message;
    private LocalDateTime timestamp;
    private Map<String, String> details; //Only for validation errors, key is field name and value is error message

    // Static factory method for creating error response without details field
    public static ErrorResponseDTO of(int status, String message) {
        return ErrorResponseDTO.builder()
                .status(status)
                .message(message)
                .timestamp(LocalDateTime.now())
                .build();
    }
}
