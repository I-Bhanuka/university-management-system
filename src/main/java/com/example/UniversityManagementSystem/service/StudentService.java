package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class StudentService {

    public void registerStudent(RegisterStudentDTO request) {

        // convert the String DOB into LocalDate
        LocalDate date = LocalDate.parse(request.getDob());
        // Get the Enrollment date
        LocalDate enrollmentDate = LocalDate.now();

        // Create the StudentId
        String studentId = "ST-20260000";

        // Split everything, assuming that studentId is retrieved from database and is in the format "ST-20260000"
        String[] parts = studentId.split("-");
        String prefix = parts[0]; // "ST"
        int yearId = Integer.parseInt(parts[1].subSequence(0, 4).toString()); // "2026"

        // Get the sub string, parse into int, and then add 1 to it
        int numberId = Integer.parseInt(parts[1].substring(4)) + 1; // "0000"

        // Check the year portion of the ID
        if (yearId != LocalDate.now().getYear()) {
            // If the year portion does not match with the current year, update the year
            yearId = LocalDate.now().getYear();

            // Reset the number portion to 0000
            numberId = 0;
        }

        // Construct the new studentId
        studentId = String.format("%s-%d%04d", prefix, yearId, numberId); // "ST-20260001";
        log.info("Student ID: {}", studentId);



        // Create the Student entity and save to database
    }

}
