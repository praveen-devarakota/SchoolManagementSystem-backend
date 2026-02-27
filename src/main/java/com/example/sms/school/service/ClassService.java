package com.example.sms.school.service;

import com.example.sms.school.dto.ClassDto;

public interface ClassService {
    void addClass(ClassDto dto);
    void deleteClass(Long id);
}