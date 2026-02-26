package com.example.sms.school.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class StudentDto {

    private String name;
    private String fatherName;
    private String motherName;
    private LocalDate dateOfBirth;
    private String email;
    private String phoneNo;

    private Long classId;
}