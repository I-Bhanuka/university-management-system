package com.example.UniversityManagementSystem.Controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@RestController // Marks that this class handles http requests and automatically make the return object json
@RequestMapping("students")
@Slf4j
public class StudentController {

    @GetMapping
    public String getAllStudents() {
        return "List of all students";
    }

    @PostMapping("register")
    public String registerStudent(@RequestBody Object object) {
        log.info("Register student {}", object);
        return "Create a new student. Name: " + object.toString();
    }

}
