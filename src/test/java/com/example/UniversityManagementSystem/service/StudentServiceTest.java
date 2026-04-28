package com.example.UniversityManagementSystem.service;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.UpdateStudentDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.entity.User;
import com.example.UniversityManagementSystem.enums.StudentStatus;
import com.example.UniversityManagementSystem.exception.BadRequestException;
import com.example.UniversityManagementSystem.exception.StudentNotFoundException;
import com.example.UniversityManagementSystem.repository.StudentRepository;
import com.example.UniversityManagementSystem.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@Slf4j
@ExtendWith(MockitoExtension.class) // Enable Mockito
@DisplayName("StudentService Tests")
class StudentServiceTest {

    // MOCKS - Fake Dependencies
    @Mock
    private StudentRepository studentRepository;

    @Mock
    private UserRepository userRepository;

    // CLASS UNDER TEST - The class we want to test
    @InjectMocks
    private StudentService studentService;

    // SHARED TEST DATA - Data that can be used across multiple tests
    private Student activeStudent;
    private StudentIdDTO studentIdDTO;

    @BeforeEach
    void setUp() {
        // Build the student ID using the current year dynamically
        String currentYearId = String.format("ST-%d%04d", LocalDate.now().getYear(), 1);

        activeStudent = Student.builder()
                .studentId(currentYearId) // → ST-20260001 this year, ST-20270001 next year
                .firstName("Kavindu")
                .lastName("Mathew")
                .email("kavindu@gmail.com")
                .dob(LocalDate.of(2000, 1, 1))
                .studentStatus(StudentStatus.ACTIVE)
                .build();

        studentIdDTO = new StudentIdDTO(
                String.format("ST-%d%04d", LocalDate.now().getYear(), 1)
        );
    }

    // ════════════════════════════════════════════════════════════════════
    // deactivateStudent() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should deactivate student successfully when student exists")
    void shouldDeactivateStudentSuccessfully() {

        // ARRANGE
        // Stub: When the repo looks for this student, return active student
        when(studentRepository.findByStudentId("ST-20260001"))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        studentService.deactivateStudent(studentIdDTO);

        // ASSERT

        // 1. Verify the student status is set to INACTIVE
        assertEquals(StudentStatus.INACTIVE, activeStudent.getStudentStatus());

        // 2. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when student does not exist when deactivating")
    void shouldThrowExceptionWhenStudentNotFoundWhenDeactivating() {

        // ARRANGE
        // Stub: when the repo looks for this student, return empty
        when(studentRepository.findByStudentId("INVALID-ID"))
                .thenReturn(Optional.empty());

        StudentIdDTO invalidRequest = new StudentIdDTO("INVALID-ID");

        // ACT and ASSERT
        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.deactivateStudent(invalidRequest)
        );

        // Verify that save() was never called, since the student was not found
        verify(studentRepository, never()).save(any(Student.class));
    }

    // ════════════════════════════════════════════════════════════════════
    // generateStudentId() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should generate student ID when there is no previous student")
    void shouldGenerateStudentIdWhenNoPreviousStudent() {

        // ARRANGE
        // Stub: when the repo looks for the last student, return empty
        when(studentRepository.findTopByOrderByCreatedAtDesc())
                .thenReturn(Optional.empty());

        // ACT
        String generatedId = studentService.generateStudentId();

        // ASSERT
        String expectedId = String.format("ST-%d0000", java.time.LocalDate.now().getYear());
        assertEquals(expectedId, generatedId);
    }

    @Test
    @DisplayName("Should generate student ID when previous student exists")
    void shouldGenerateStudentIdWhenPreviousStudentExists() {

        // ARRANGE
        // Stub: when the repo looks for the last student, return activeStudent
        when(studentRepository.findTopByOrderByCreatedAtDesc())
                .thenReturn(Optional.of(activeStudent));

        // ACT
        String generatedId = studentService.generateStudentId();

        // ASSERT
        String expectedId = String.format("ST-%d%04d", LocalDate.now().getYear(), 2);
        assertEquals(expectedId, generatedId);
    }

    // ════════════════════════════════════════════════════════════════════
    // registerStudent() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should successfully register a new student")
    void shouldRegisterStudent() {

        // ARRANGE
        RegisterStudentDTO request = new RegisterStudentDTO(
                "John",
                "Doe",
                "2000-01-01",
                "doe@gmail.com");

        // ACT
        studentService.registerStudent(request);

        // ASSERT
        // Verify that save() was called once, meaning the student was registered
        verify(studentRepository, times(1)).save(any(Student.class));

        // Verify that save() of userRepository was called once, meaning the user account was created
        verify(userRepository, times(1)).save(any(User.class));

    }


    // ════════════════════════════════════════════════════════════════════
    // getStudent() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should return student by student ID when student exists")
    void shouldRetrieveStudentWhenExists() {

        // ARRANGE
        String studentId = activeStudent.getStudentId(); // ← pull from the object

        when(studentRepository.findByStudentId(studentId))
                .thenReturn(Optional.of(activeStudent));

        StudentIdDTO request = new StudentIdDTO(studentId);

        // ACT
        Student result = studentService.getStudent(request);

        // ASSERT
        assertNotNull(result);
        assertEquals(studentId, result.getStudentId());           // ← dynamic
        assertEquals("Kavindu", result.getFirstName());
        assertEquals("Mathew", result.getLastName());
        assertEquals("kavindu@gmail.com", result.getEmail());
    }

    @Test
    @DisplayName("Should throw StudentNotFoundException when student does not exist when retrieving")
    void shouldThrowExceptionWhenStudentNotFoundWhenRetrieving() {
        // ARRANGE
        when(studentRepository.findByStudentId("INVALID-ID"))
                .thenReturn(Optional.empty());

        StudentIdDTO request = new StudentIdDTO("INVALID-ID");

        // ACT and ASSERT
        assertThrows(
                StudentNotFoundException.class,
                () -> studentService.getStudent(request)
        );
    }

    // ════════════════════════════════════════════════════════════════════
    // getAllStudents() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should return all students when students exist")
    void shouldRetrieveAllStudentsWhenExits() {
        // ARRANGE
        when(studentRepository.findAll())
                .thenReturn(java.util.List.of(activeStudent));

        // ACT
        List<Student> result = studentService.getAllStudents();

        // ASSERT
        assertNotNull(result);
        assertEquals(1, result.size());
    }

    @Test
    @DisplayName("Should return empty list when no students exist")
    void shouldRetrieveNoneWhenNoStudentsExist() {
        // ARRANGE
        when(studentRepository.findAll())
                .thenReturn(java.util.Collections.emptyList());

        // ACT
        List<Student> result = studentService.getAllStudents();

        // ASSERT
        assertTrue(result.isEmpty());
    }


    // ════════════════════════════════════════════════════════════════════
    // updateStudent() tests
    // ════════════════════════════════════════════════════════════════════

    @Test
    @DisplayName("Should throw BadRequestException when no fields provided to update")
    void shouldThrowBadRequestWhenNoFieldsProvidedToUpdate() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with only studentId and no other fields
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));


        // ACT and ASSERT
        assertThrows(BadRequestException.class,
                () -> studentService.updateStudent(request));

    }

    @Test
    @DisplayName("Should update all fields successfully when all fields provided")
    void shouldUpdateAllFields() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with all fields to update
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .firstName("UpdatedFirstName")
                .lastName("UpdatedLastName")
                .email("updatedEmail")
                .dob(LocalDate.of(1995, 5, 15))
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        Student updatedStudent = studentService.updateStudent(request);

        // ASSERT
        // 1. Verify all fields are updated correctly
        assertEquals("UpdatedFirstName", updatedStudent.getFirstName());
        assertEquals("UpdatedLastName", updatedStudent.getLastName());
        assertEquals("updatedEmail", updatedStudent.getEmail());
        assertEquals(LocalDate.of(1995, 5, 15), updatedStudent.getDob());

        // 2. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);
    }


    @Test
    @DisplayName("Should update only first name when only first name provided")
    void shouldUpdateFirstNameOnly() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with only first name to update
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .firstName("UpdatedFirstName")
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        Student updatedStudent = studentService.updateStudent(request);

        // ASSERT
        // 1. Verify only first name is updated correctly
        assertEquals("UpdatedFirstName", updatedStudent.getFirstName());

        // 2. Verify other fields remain unchanged
        assertEquals(activeStudent.getLastName(), updatedStudent.getLastName());
        assertEquals(activeStudent.getEmail(), updatedStudent.getEmail());
        assertEquals(activeStudent.getDob(), updatedStudent.getDob());

        // 3. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);
    }

    @Test
    @DisplayName("Should update only last name when only last name provided")
    void shouldUpdateLastNameOnly() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with only last name to update
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .lastName("UpdatedLastName")
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        Student updatedStudent = studentService.updateStudent(request);

        // ASSERT
        // 1. Verify only first name is updated correctly
        assertEquals("UpdatedLastName", updatedStudent.getLastName());

        // 2. Verify other fields remain unchanged
        assertEquals(activeStudent.getFirstName(), updatedStudent.getFirstName());
        assertEquals(activeStudent.getEmail(), updatedStudent.getEmail());
        assertEquals(activeStudent.getDob(), updatedStudent.getDob());

        // 3. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);

    }

    @Test
    @DisplayName("Should update only Dob when only Dob provided")
    void shouldUpdateDobOnly() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with only the dob to update
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .dob(LocalDate.of(1995, 5, 15))
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        Student updatedStudent = studentService.updateStudent(request);

        // ASSERT
        // 1. Verify only first name is updated correctly
        assertEquals(LocalDate.of(1995, 5, 15), updatedStudent.getDob());

        // 2. Verify other fields remain unchanged
        assertEquals(activeStudent.getFirstName(), updatedStudent.getFirstName());
        assertEquals(activeStudent.getLastName(), updatedStudent.getLastName());
        assertEquals(activeStudent.getEmail(), updatedStudent.getEmail());

        // 3. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);

    }

    @Test
    @DisplayName("Should update only email when only email provided")
    void shouldUpdateEmailOnly() {
        // ARRANGE
        // 1. Create the UpdateStudentDTO with only email to update
        UpdateStudentDTO request = UpdateStudentDTO.builder()
                .studentId(activeStudent.getStudentId())
                .email("updatedEmail")
                .build();

        // 2. Stub: when the repo looks for this student, return active student
        when(studentRepository.findByStudentId(request.getStudentId()))
                .thenReturn(Optional.of(activeStudent));

        // ACT
        Student updatedStudent = studentService.updateStudent(request);

        // ASSERT
        // 1. Verify only first name is updated correctly
        assertEquals("updatedEmail", updatedStudent.getEmail());

        // 2. Verify other fields remain unchanged
        assertEquals(activeStudent.getFirstName(), updatedStudent.getFirstName());
        assertEquals(activeStudent.getLastName(), updatedStudent.getLastName());
        assertEquals(activeStudent.getDob(), updatedStudent.getDob());

        // 3. Verify save() was called once, meaning the change was persisted
        verify(studentRepository, times(1)).save(activeStudent);
    }



    @Test
    void searchStudent() {
    }

    @Test
    void findStudentByStudentId() {
    }



}