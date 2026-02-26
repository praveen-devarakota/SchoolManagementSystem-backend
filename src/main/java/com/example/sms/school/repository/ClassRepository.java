package com.example.sms.school.repository;

import com.example.sms.school.entity.SchoolClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClassRepository extends JpaRepository<SchoolClass, Long> {
}