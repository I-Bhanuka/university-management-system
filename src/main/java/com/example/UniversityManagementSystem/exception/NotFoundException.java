package com.example.UniversityManagementSystem.exception;

public class NotFoundException extends ApplicationException {
    public NotFoundException(String message) {
        super(message, 404);
    }
}
