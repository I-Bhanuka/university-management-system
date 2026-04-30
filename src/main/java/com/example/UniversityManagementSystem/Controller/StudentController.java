package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.ResponseDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.StudentResponseDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.service.impl.StudentServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;

@RestController // Marks that this class handles http requests and automatically make the return object JSON
@RequestMapping("/api/students")
@SecurityRequirement(name = "bearerAuth")
@RequiredArgsConstructor // Will generate a constructor for fields with final keyword, which will be used for dependency injection of StudentServiceImpl
public class StudentController {

    private final StudentServiceImpl studentService; // Dependency injection will be done by RequiredArgsConstructor

    // Get all students without pagination
    @GetMapping
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    // Get all sorted students without pagination
    @GetMapping("/sorted")
    public List<Student> getAllStudentsSorted(Sort sort) {
        return studentService.getAllStudentsSorted(sort);
    }


    // Get Pagination and Sorted
    @GetMapping("/paginatedAndSorted")
    public Page<Student> getAllStudentsPaginatedAndSorted(Pageable pageable) {
        // If the user passes the Pagination parameters, then it will be automatically converted to Pageable object by Spring
        // And if the user does not pass the Pagination parameters, then it will be null and we can handle it in the service layer
        return studentService.getAllStudentsPaginatedAndSorted(pageable);
    }

    @PostMapping("/getStudent")
    public Student getStudent(@Valid @RequestBody StudentIdDTO request) {
        return studentService.getStudent(request);
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO> registerStudent(@Valid @RequestBody RegisterStudentDTO request) {
        studentService.registerStudent(request);

        return ResponseEntity.ok(new ResponseDTO("Student created Successfully"));
    }

    @PostMapping("/deactivate")
    public ResponseEntity<ResponseDTO> deactivateStudent(@Valid @RequestBody StudentIdDTO request) {
        studentService.deactivateStudent(request);

        return ResponseEntity.ok(new ResponseDTO("Student deactivated Successfully"));
    }

    @PatchMapping("/update")
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody UpdateStudentDTO request) {


        Student updatedStudent = studentService.updateStudent(request);

        return  ResponseEntity.status(200).body(updatedStudent);
    }


    // Custom search endpoint to search for a student by name or email
    @GetMapping("/search")
    public ArrayList<StudentResponseDTO> searchStudent(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email) {

        return studentService.searchStudent(firstName, email);
    }


}
