package com.example.UniversityManagementSystem.service.impl;

import com.example.UniversityManagementSystem.dto.requestDTOs.NameEmailStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.StudentResponseDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.entity.User;
import com.example.UniversityManagementSystem.enums.Role;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.exception.BadRequestException;
import com.example.UniversityManagementSystem.exception.StudentNotFoundException;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import com.example.UniversityManagementSystem.repository.UserRepository;
import com.example.UniversityManagementSystem.service.StudentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    // Anyone authenticated can view a students
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Override
    public Student getStudent(StudentIdDTO request) {
        // Find the Student
        return findStudentByStudentId(request.getStudentId());
    }

    // Anyone authenticated can view all students
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Override
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Transactional // Rollback
    @Override
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

        // Create a user entity as well, and assign the STUDENT role to the user
        User saveAsUser = User.builder()
                .username(request.getFirstName() + request.getLastName())
                .password("defaultPassword") // You can set a default password or generate one
                .role(Role.ROLE_STUDENT)
                .build();

        // Save the user entity to the database
        userRepository.save(saveAsUser);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public void deactivateStudent(StudentIdDTO request) {
        // Find the Student
        Student std = findStudentByStudentId(request.getStudentId());

        // Update the student status to INACTIVE
        std.setStudentStatus(StudentStatus.INACTIVE);

        // Save the updated Student entity to the database
        studentRepository.save(std);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    @Override
    public Student updateStudent(UpdateStudentDTO request) {

        boolean updated = false;

        // Find the Student
        Student std = findStudentByStudentId(request.getStudentId());

        // Update the student information
        if (request.getFirstName() != null) {
            if (request.getFirstName().isEmpty()) {
                log.warn("Empty first name provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for first name of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: First name cannot be empty.");
            }

            if (request.getFirstName().equals(std.getFirstName())) {
                log.warn("Same first name provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for first name of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: First name is the same as the current value.");
            }

            std.setFirstName(request.getFirstName());
            updated = true;
        }

        if (request.getLastName() != null) {
            if  (request.getLastName().isEmpty()) {
                log.warn("Empty last name provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for last name of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: Last name cannot be empty.");
            }

            if (request.getLastName().equals(std.getLastName())) {
                log.warn("Same last name provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for last name of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: Last name is the same as the current value.");
            }

            std.setLastName(request.getLastName());
            updated = true;
        }

        if (request.getDob() != null) {
            std.setDob(request.getDob());
            updated = true;
        }

        if (request.getEmail() != null) {
            if (request.getEmail().isEmpty()) {
                log.warn("Empty email provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for email of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: Email cannot be empty.");
            }

            if (request.getEmail().equals(std.getEmail())) {
                log.warn("Same email provided for student with ID: {}", request.getStudentId());
                log.info("Skipping update for email of student with ID: {}", request.getStudentId());
                throw new BadRequestException("Update failed: Email is the same as the current value.");
            }

            std.setEmail(request.getEmail());
            updated = true;
        }

        if (!updated) {
            log.warn("No fields provided to update for student with ID: {}", request.getStudentId());
            log.info("Skipping update for student with ID: {}", request.getStudentId());
            throw new BadRequestException("Update failed: At least one field (firstName, lastName, dob, email) must be provided for update.");
        }

        // Save the changes to the database
        studentRepository.save(std);

        return std;
    }

    // Anyone authenticated can search
    @PreAuthorize("hasAnyRole('ADMIN', 'STUDENT')")
    @Override
    public ArrayList<StudentResponseDTO> searchStudent(String firstName, String email) {
//        Student std = null;
//
//        if (request.getFirstName() != null && request.getEmail() != null){
//            std = studentRepository.findByFirstNameAndEmail(request.getFirstName(), request.getEmail()).orElse(null);
//        } else if (request.getEmail() != null) {
//            std = studentRepository.findByEmail(request.getEmail()).orElse(null);
//        } else if (request.getFirstName() != null)  {
//            std = studentRepository.findTopByFirstName(request.getFirstName()).orElse(null);
//        }
//
//        if (std == null) {
//            log.info("Student with name {} or email {} not found", request.getFirstName(), request.getEmail());
//            throw new StudentNotFoundException(request.getFirstName());
//        } else  {
//            return std;
//        }

        List<Student> result = studentRepository.findStudentByFirstNameAndEmail(firstName, email);

        if (result.isEmpty()) {
            log.info("No records were found with Students with name {} or email {}.",firstName, email);
            throw new StudentNotFoundException("Name: " + firstName + " or Email " + email);
        }

        log.info("================== Search Results =================");
        log.info("Search criteria - First Name: {}, Email: {}", firstName, email);

        // Convert the search results into List of StudentResponseDTO for better logging and response formatting
        ArrayList<StudentResponseDTO> responseDTO = new ArrayList<>();
        for (Student student : result) {
            log.info("Student found with Student Id: {} First Name: {}, Last Name: {}, Email: {}, DOB: {}, Enrollment Date: {}, Status: {}",
                    student.getStudentId(), student.getFirstName(), student.getLastName(), student.getEmail(),
                    student.getDob(), student.getEnrollmentDate(), student.getStudentStatus());

            responseDTO.add(StudentResponseDTO.builder()
                            .firstName(student.getFirstName())
                            .lastName(student.getLastName())
                            .email(student.getEmail())
                            .dob(student.getDob())
                            .enrollmentDate(student.getEnrollmentDate())
                            .studentStatus(student.getStudentStatus())
                            .course(student.getCourse())
                            .build());

        }

        return responseDTO;
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
            throw new StudentNotFoundException("Id: " + studentId);
        }

        return std;
    }

}
