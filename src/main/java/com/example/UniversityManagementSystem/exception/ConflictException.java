package com.example.UniversityManagementSystem.exception;

public class ConflictException extends ApplicationException {
    public ConflictException(String message) {
        super(message, 409);
    }
}
