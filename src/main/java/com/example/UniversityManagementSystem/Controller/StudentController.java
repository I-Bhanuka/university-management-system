package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.*;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks that this class handles http requests and automatically make the return object json
@RequestMapping("/api/students")
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
    public ResponseEntity<ResponseDTO> updateStudent(@Valid @RequestBody UpdateStudentDTO request) {

        log.info(request.getStudentId());

        studentService.updateStudent(request);

        return  ResponseEntity.ok(new ResponseDTO("Student updated Successfully"));
    }


    // Custom search endpoint to search for a student by name or email
    @GetMapping("/search")
    public Student searchStudent(@Valid @RequestBody NameEmailStudentDTO request) {
        return studentService.searchStudent(request);
    }


}
