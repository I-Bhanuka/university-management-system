package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.ResponseDTO;
import com.example.UniversityManagementSystem.dto.StudentIdDTO;
import com.example.UniversityManagementSystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController // Marks that this class handles http requests and automatically make the return object json
@RequestMapping("students")
@Slf4j
public class StudentController {

    private final StudentService studentService;

    // Dependency injection - Constructor injection
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping
    public String getAllStudents() {
        return "List of all students";
    }

    @PostMapping()
    public ResponseEntity<ResponseDTO> registerStudent(@Valid @RequestBody RegisterStudentDTO request) {
        studentService.registerStudent(request);

        return ResponseEntity.ok(new ResponseDTO("Student created Successfully"));
    }

    @GetMapping("/deactivate")
    public ResponseEntity<ResponseDTO> deactivateStudent(@Valid @RequestBody StudentIdDTO request) {
        studentService.deactivateStudent(request);

        return ResponseEntity.ok(new ResponseDTO("Student deactivated Successfully"));
    }

}
