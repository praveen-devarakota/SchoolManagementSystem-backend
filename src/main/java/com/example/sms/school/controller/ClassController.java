package com.example.sms.school.controller;

import com.example.sms.school.dto.ClassDto;
import com.example.sms.school.service.ClassService;
import com.example.sms.util.SchoolContextHolder;
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

        // ✅ ENSURE CONTEXT
        SchoolContextHolder.setSchoolCode(schoolCode);
        try {
            classService.addClass(dto);
            return ResponseEntity.ok("Class added successfully");
        } finally {
            SchoolContextHolder.clear();
        }
    }

    // ✅ DELETE ENDPOINT (THIS WAS MISSING)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteClass(
            @PathVariable String schoolCode,
            @PathVariable Long id) {

        // ✅ ENSURE CONTEXT
        SchoolContextHolder.setSchoolCode(schoolCode);
        try {
            classService.deleteClass(id);
            return ResponseEntity.ok("Class deleted successfully");
        } finally {
            SchoolContextHolder.clear();
        }
    }
}