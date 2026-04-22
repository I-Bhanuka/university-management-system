package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.ResponseDTO;
import com.example.UniversityManagementSystem.dto.StudentIdDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    public List<Student> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/getStudent")
    public Student getStudent(@Valid @RequestBody StudentIdDTO request) {
        return studentService.getStudent(request);
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
