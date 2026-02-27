package com.example.sms.school.controller;

import com.example.sms.school.dto.ClassDto;
import com.example.sms.school.service.ClassService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/schools/{schoolCode}/classes")
public class ClassController {

    private final ClassService classService;

    public ClassController(ClassService classService) {
        this.classService = classService;
    }

    @PostMapping
    public ResponseEntity<String> addClass(
            @PathVariable String schoolCode,
            @RequestBody ClassDto dto) {

        classService.addClass(dto);

        return ResponseEntity.ok("Class added successfully");
    }
}