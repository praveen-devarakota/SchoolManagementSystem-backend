package com.example.sms.master.repository;

import com.example.sms.master.entity.School;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SchoolRepository extends JpaRepository<School, Long> {

    boolean existsBySchoolCode(String schoolCode);

    Optional<School> findBySchoolCode(String schoolCode);
}