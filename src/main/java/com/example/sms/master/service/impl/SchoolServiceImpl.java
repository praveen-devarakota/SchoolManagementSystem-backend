package com.example.sms.master.service.impl;

import com.example.sms.enums.SchoolStatus;
import com.example.sms.master.dto.SchoolRequestDto;
import com.example.sms.master.entity.School;
import com.example.sms.master.repository.SchoolRepository;
import com.example.sms.master.service.SchoolService;
import com.example.sms.util.DatabaseUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final DatabaseUtil databaseUtil;

    public SchoolServiceImpl(SchoolRepository schoolRepository,
                             DatabaseUtil databaseUtil) {
        this.schoolRepository = schoolRepository;
        this.databaseUtil = databaseUtil;
    }

    @Override
    public void createSchool(SchoolRequestDto dto) {

        if (schoolRepository.existsBySchoolCode(dto.getSchoolCode())) {
            throw new RuntimeException(
                    "School already exists with code: " + dto.getSchoolCode()
            );
        }

        // DB name is IMMUTABLE
        String dbName = "sms_school_" + dto.getSchoolCode().toLowerCase();

        School school = new School();
        school.setSchoolCode(dto.getSchoolCode());
        school.setSchoolName(dto.getSchoolName());
        school.setDbName(dbName);
        school.setDbUsername("root");   // move to env later
        school.setDbPassword("root");
        school.setStatus(SchoolStatus.ACTIVE);

        schoolRepository.save(school);

        databaseUtil.createDatabase(dbName);
        databaseUtil.runSchema(dbName);
    }

    @Override
    public void updateSchool(Long id, SchoolRequestDto dto) {

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));

        // Validate new school code
        if (!school.getSchoolCode().equals(dto.getSchoolCode())
                && schoolRepository.existsBySchoolCode(dto.getSchoolCode())) {
            throw new RuntimeException("School code already exists");
        }

        // Update MASTER DB
        school.setSchoolCode(dto.getSchoolCode());
        school.setSchoolName(dto.getSchoolName());
        schoolRepository.save(school);

        // Update SCHOOL DB metadata
        databaseUtil.updateSchoolMetadata(
                school.getDbName(),
                dto.getSchoolCode(),
                dto.getSchoolName()
        );
    }

    @Override
    public void deleteSchool(Long id) {

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));

        if (school.getStatus() == SchoolStatus.ACTIVE) {
            throw new RuntimeException(
                    "Deactivate school before permanent deletion"
            );
        }

        // 1️⃣ Delete school DB
        databaseUtil.dropDatabase(school.getDbName());

        // 2️⃣ Delete master record
        schoolRepository.delete(school);
    }

    @Override
    public void deactivateSchool(Long id) {

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));

        if (school.getStatus() == SchoolStatus.INACTIVE) {
            throw new RuntimeException("School is already inactive");
        }

        if (school.getStatus() == SchoolStatus.DELETED) {
            throw new RuntimeException("Deleted school cannot be deactivated");
        }

        school.setStatus(SchoolStatus.INACTIVE);
        schoolRepository.save(school);
    }

    @Override
    public void activateSchool(Long id) {

        School school = schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));

        if (school.getStatus() == SchoolStatus.ACTIVE) {
            throw new RuntimeException("School is already active");
        }

        if (school.getStatus() == SchoolStatus.DELETED) {
            throw new RuntimeException("Deleted school cannot be activated");
        }

        school.setStatus(SchoolStatus.ACTIVE);
        schoolRepository.save(school);
    }

    @Override
    @Transactional(readOnly = true)
    public List<School> getAllSchools() {
        return schoolRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public School getSchoolById(Long id) {
        return schoolRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("School not found"));
    }
}