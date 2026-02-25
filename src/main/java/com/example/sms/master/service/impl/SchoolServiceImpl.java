package com.example.sms.master.service.impl;

import com.example.sms.enums.SchoolStatus;
import com.example.sms.master.dto.SchoolRequestDto;
import com.example.sms.master.entity.School;
import com.example.sms.master.repository.SchoolRepository;
import com.example.sms.master.service.SchoolService;
import com.example.sms.util.DatabaseUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class SchoolServiceImpl implements SchoolService {

    private final SchoolRepository schoolRepository;
    private final DatabaseUtil databaseUtil;

    @Autowired
    public SchoolServiceImpl(SchoolRepository schoolRepository,
                             DatabaseUtil databaseUtil) {
        this.schoolRepository = schoolRepository;
        this.databaseUtil = databaseUtil;
    }

    @Override
    public void createSchool(SchoolRequestDto dto) {

        // 1️⃣ Validation
        if (schoolRepository.existsBySchoolCode(dto.getSchoolCode())) {
            throw new RuntimeException(
                    "School already exists with code: " + dto.getSchoolCode()
            );
        }

        // 2️⃣ Prepare DB name
        String dbName = "sms_school_" + dto.getSchoolCode().toLowerCase();

        // 3️⃣ Save metadata in MASTER DB
        School school = new School();
        school.setSchoolCode(dto.getSchoolCode());
        school.setSchoolName(dto.getSchoolName());
        school.setDbName(dbName);
        school.setDbUsername("root");   // later: move to env
        school.setDbPassword("root");
        school.setStatus(SchoolStatus.ACTIVE);

        schoolRepository.save(school);

        // 4️⃣ Create school database
        databaseUtil.createDatabase(dbName);

        // 5️⃣ Initialize school schema
        databaseUtil.runSchema(dbName);
    }
}