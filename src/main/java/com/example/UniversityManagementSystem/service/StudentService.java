package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class StudentService {

    public void registerStudent(RegisterStudentDTO request) {

        log.info("Register Student Request");
    }

}
