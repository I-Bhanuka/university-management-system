package com.example.UniversityManagementSystem.exception;

public class StudentNotFoundException extends NotFoundException {
    public StudentNotFoundException(String studentId) {
        super("Student not found with ID: " + studentId);
    }
}
