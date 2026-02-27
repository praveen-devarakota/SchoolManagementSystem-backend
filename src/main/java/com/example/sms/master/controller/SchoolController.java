package com.example.sms.master.controller;

import com.example.sms.master.dto.SchoolRequestDto;
import com.example.sms.master.entity.School;
import com.example.sms.master.service.SchoolService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/superadmin/schools")
public class SchoolController {

    private final SchoolService schoolService;

    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    // ✅ CREATE SCHOOL
    @PostMapping
    public ResponseEntity<String> createSchool(@RequestBody SchoolRequestDto dto) {
        schoolService.createSchool(dto);
        return ResponseEntity.ok("School created successfully");
    }

    // ✅ GET ALL SCHOOLS (SuperAdmin list view)
    @GetMapping
    public ResponseEntity<List<School>> getAllSchools() {
        return ResponseEntity.ok(schoolService.getAllSchools());
    }

    // ✅ GET SCHOOL BY ID (click / details view)
    @GetMapping("/{id}")
    public ResponseEntity<School> getSchoolById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolService.getSchoolById(id));
    }

    // ✅ UPDATE SCHOOL (code + name)
    @PutMapping("/{id}")
    public ResponseEntity<String> updateSchool(
            @PathVariable Long id,
            @RequestBody SchoolRequestDto dto) {

        schoolService.updateSchool(id, dto);
        return ResponseEntity.ok("School updated successfully");
    }

    // ✅ DEACTIVATE SCHOOL
    @PutMapping("/{id}/deactivate")
    public ResponseEntity<String> deactivateSchool(@PathVariable Long id) {
        schoolService.deactivateSchool(id);
        return ResponseEntity.ok("School deactivated successfully");
    }

    // ✅ ACTIVATE SCHOOL
    @PutMapping("/{id}/activate")
    public ResponseEntity<String> activateSchool(@PathVariable Long id) {
        schoolService.activateSchool(id);
        return ResponseEntity.ok("School activated successfully");
    }

    // ✅ DELETE SCHOOL (only when INACTIVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSchool(@PathVariable Long id) {
        schoolService.deleteSchool(id);
        return ResponseEntity.ok("School deleted successfully");
    }
}