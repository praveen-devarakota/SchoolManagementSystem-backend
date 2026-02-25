package com.example.sms.master.repository;

import com.example.sms.master.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SchoolRepository extends JpaRepository<School, Long> {
    boolean existsBySchoolCode(String schoolCode);
}
