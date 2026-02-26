package com.example.sms.school.service.impl;

import com.example.sms.school.dto.StudentDto;
import com.example.sms.school.entity.SchoolClass;
import com.example.sms.school.entity.Student;
import com.example.sms.school.repository.ClassRepository;
import com.example.sms.school.repository.StudentRepository;
import com.example.sms.school.service.StudentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class StudentServiceImpl implements StudentService {

    private final StudentRepository studentRepository;
    private final ClassRepository classRepository;

    public StudentServiceImpl(StudentRepository studentRepository,
                              ClassRepository classRepository) {
        this.studentRepository = studentRepository;
        this.classRepository = classRepository;
    }

    @Override
    public void addStudent(StudentDto dto) {

        SchoolClass schoolClass = classRepository.findById(dto.getClassId())
                .orElseThrow(() -> new RuntimeException("Class not found"));

        Student student = new Student();
        student.setName(dto.getName());
        student.setFatherName(dto.getFatherName());
        student.setMotherName(dto.getMotherName());
        student.setDateOfBirth(dto.getDateOfBirth());
        student.setEmail(dto.getEmail());
        student.setPhoneNo(dto.getPhoneNo());
        student.setSchoolClass(schoolClass);

        studentRepository.save(student);
    }
}