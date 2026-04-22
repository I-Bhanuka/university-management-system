package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public void registerStudent(RegisterStudentDTO request) {

        // convert the String DOB into LocalDate
        LocalDate date = LocalDate.parse(request.getDob());
        // Get the Enrollment date
        LocalDate enrollmentDate = LocalDate.now();

        // Create the StudentId
        String studentId = generateStudentId();
        log.info(studentId);

        // Create a new Student entity using the builder pattern
        Student student = Student.builder()
                .studentId(studentId)
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .dob(date)
                .email(request.getEmail())
                .enrollmentDate(enrollmentDate)
                .studentStatus(StudentStatus.ACTIVE)
                .build();

        // Save the Student entity to the database
        studentRepository.save(student);
    }



    // Helper method to generate studentId
    public String generateStudentId() {
        String studentId;

        // Retrieve the last studentId from database, and if there is no student in database
        Student std = studentRepository.findTopByOrderByCreatedAtDesc().orElse(null);

        if (std == null) {
            // if there is no student in database, create the first studentId with the format "ST-YYYY0000"
            studentId = String.format("%s-%d%04d", "ST", LocalDate.now().getYear(), 0);
            return studentId;

        }

        // Split everything
        studentId = std.getStudentId();
        String[] parts = studentId.split("-");
        String prefix = parts[0]; // "ST"
        int yearId = Integer.parseInt(parts[1].subSequence(0, 4).toString()); // "2026"

        // Get the sub string, parse into int, and then add 1 to it
        int numberId = Integer.parseInt(parts[1].substring(4)) + 1;

        // Check the year portion of the ID
        if (yearId != LocalDate.now().getYear()) {
            // If the year portion does not match with the current year, update the year
            yearId = LocalDate.now().getYear();

            // Reset the number portion to 0000
            numberId = 0;
        }

        // Construct the new studentId
        studentId = String.format("%s-%d%04d", prefix, yearId, numberId); // "ST-20260001";
        return studentId;
    }

}
