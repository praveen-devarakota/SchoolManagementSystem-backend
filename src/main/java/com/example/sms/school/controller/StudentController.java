package com.example.sms.school.controller;

import com.example.sms.school.dto.StudentDto;
import com.example.sms.school.service.StudentService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools/{schoolCode}/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    @PostMapping
    public ResponseEntity<String> addStudent(
            @PathVariable String schoolCode,
            @RequestBody StudentDto dto) {

        // schoolCode used later for datasource routing
        studentService.addStudent(dto);
        return ResponseEntity.ok("Student added successfully");
    }
}