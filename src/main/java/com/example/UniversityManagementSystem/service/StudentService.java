package com.example.UniversityManagementSystem.service;

import ch.qos.logback.classic.boolex.StubEventEvaluator;
import com.example.UniversityManagementSystem.dto.NameEmailStudentDTO;
import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.UpdateStudentDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.exception.StudentNotFoundException;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Slf4j
@Service
public class StudentService {

    private final StudentRepository studentRepository;

    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public Student getStudent(StudentIdDTO request) {
        // Find the Student
        return findStudentByStudentId(request.getStudentId());
    }

    public List<Student> getAllStudents() {
        return studentRepository.findAll();
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

        // Logs
        log.info("Registering student with ID: {}", student.getStudentId());
        log.info("First Name: {}", student.getFirstName());
        log.info("Last Name: {}", student.getLastName());
        log.info("Date of Birth: {}", student.getDob());
        log.info("Email: {}", student.getEmail());
        log.info("Enrollment Date: {}", student.getEnrollmentDate());

        // Save the Student entity to the database
        studentRepository.save(student);
    }

    public void deactivateStudent(StudentIdDTO request) {
        // Find the Student
        Student std = findStudentByStudentId(request.getStudentId());

        // Update the student status to INACTIVE
        std.setStudentStatus(StudentStatus.INACTIVE);

        // Save the updated Student entity to the database
        studentRepository.save(std);
    }

    public void updateStudent(UpdateStudentDTO request) {
        // Find the Student
        Student std = findStudentByStudentId(request.getStudentId());

        // Update the student information
        if (request.getFirstName() != null) {
            std.setFirstName(request.getFirstName());
        }

        if (request.getLastName() != null) {
            std.setLastName(request.getLastName());
        }

        if (request.getDob() != null) {
            std.setDob(request.getDob());
        }

        if (request.getEmail() != null) {
            std.setEmail(request.getEmail());
        }

        // Save the changes to the database
        studentRepository.save(std);
    }

    public Student searchStudent(NameEmailStudentDTO request) {
        Student std = null;

        if (request.getFirstName() != null && request.getEmail() != null){
            std = studentRepository.findByFirstNameAndEmail(request.getFirstName(), request.getEmail()).orElse(null);
        } else if (request.getEmail() != null) {
            std = studentRepository.findByEmail(request.getEmail()).orElse(null);
        } else if (request.getFirstName() != null)  {
            std = studentRepository.findTopByFirstName(request.getFirstName()).orElse(null);
        }

        if (std == null) {
            log.info("Student with name {} or email {} not found", request.getFirstName(), request.getEmail());
            throw new RuntimeException("Student not found");
        } else  {
            return std;
        }
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

    // Helper method to find the student by studentId
    public Student findStudentByStudentId(String studentId) {
        // Find the Student
        Student std = studentRepository.findByStudentId(studentId).orElse(null);

        // If student is not found, log the error and throw an exception
        if (std == null){
            log.info("Student with studentId {} not found", studentId);
            throw new StudentNotFoundException(studentId);
        }

        return std;
    }

}
