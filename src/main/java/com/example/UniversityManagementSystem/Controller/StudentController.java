package com.example.UniversityManagementSystem.Controller;

import com.example.UniversityManagementSystem.dto.requestDTOs.NameEmailStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.RegisterStudentDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.StudentIdDTO;
import com.example.UniversityManagementSystem.dto.requestDTOs.UpdateStudentDTO;
import com.example.UniversityManagementSystem.dto.responseDTOs.ResponseDTO;
import com.example.UniversityManagementSystem.entity.Student;
import com.example.UniversityManagementSystem.service.impl.StudentServiceImpl;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // Marks that this class handles http requests and automatically make the return object JSON
@RequestMapping("/api/students")
@SecurityRequirement(name = "bearerAuth")
@Slf4j
public class StudentController {

    private final StudentServiceImpl studentService;

    // Dependency injection - Constructor injection
    public StudentController(StudentServiceImpl studentService) {
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
    public ResponseEntity<Student> updateStudent(@Valid @RequestBody UpdateStudentDTO request) {

        log.info(request.getStudentId());

        Student updatedStudent = studentService.updateStudent(request);

        return  ResponseEntity.status(200).body(updatedStudent);
    }


    // Custom search endpoint to search for a student by name or email
    @GetMapping("/search")
    public Student searchStudent(
            @RequestParam(required = false) String firstName,
            @RequestParam(required = false) String email) {

        // Check
        NameEmailStudentDTO request = NameEmailStudentDTO.builder()
                .firstName(firstName)
                .email(email)
                .build();

        return studentService.searchStudent(request);
    }


}
