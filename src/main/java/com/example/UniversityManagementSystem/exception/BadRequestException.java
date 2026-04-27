package com.example.UniversityManagementSystem.exception;

public class BadRequestException extends ApplicationException {
    public BadRequestException(String message) {
        super(message, 400);
    }
}
