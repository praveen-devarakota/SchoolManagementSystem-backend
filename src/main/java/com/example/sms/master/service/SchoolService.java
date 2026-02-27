package com.example.sms.master.service;

import com.example.sms.master.dto.SchoolRequestDto;
import com.example.sms.master.entity.School;

import java.util.List;

public interface SchoolService {

    void createSchool(SchoolRequestDto dto);

    void updateSchool(Long id, SchoolRequestDto dto);

    void deleteSchool(Long id);

    void deactivateSchool(Long id);

    void activateSchool(Long id);

    List<School> getAllSchools();

    School getSchoolById(Long id);
}