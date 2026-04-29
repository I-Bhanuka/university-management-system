package com.example.UniversityManagementSystem.exception;

public class StudentNotFoundException extends NotFoundException {
    public StudentNotFoundException(String info) {
        super("Student not found with, " + info);
    }
}
