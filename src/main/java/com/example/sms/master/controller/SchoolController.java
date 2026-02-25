package com.example.sms.master.controller;


import com.example.sms.master.dto.SchoolRequestDto;
import com.example.sms.master.service.SchoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/superadmin/schools")
public class SchoolController {

    private final SchoolService schoolService;

    @Autowired
    public SchoolController(SchoolService schoolService) {
        this.schoolService = schoolService;
    }

    @PostMapping
    public ResponseEntity<String> createSchool(
            @RequestBody SchoolRequestDto dto) {

        schoolService.createSchool(dto);
        return ResponseEntity.ok("School created successfully");
    }
}
