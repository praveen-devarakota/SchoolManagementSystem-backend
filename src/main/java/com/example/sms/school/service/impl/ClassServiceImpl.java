package com.example.sms.school.service.impl;

import com.example.sms.school.dto.ClassDto;
import com.example.sms.school.entity.SchoolClass;
import com.example.sms.school.repository.ClassRepository;
import com.example.sms.school.service.ClassService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClassServiceImpl implements ClassService {

    private final ClassRepository classRepository;

    public ClassServiceImpl(ClassRepository classRepository) {
        this.classRepository = classRepository;
    }

    @Override
    @Transactional(transactionManager = "schoolTransactionManager")
    public void addClass(ClassDto dto) {

        SchoolClass schoolClass = new SchoolClass();
        schoolClass.setClassName(dto.getClassName());
        schoolClass.setSection(dto.getSection());

        classRepository.save(schoolClass);
    }

    @Override
    @Transactional(transactionManager = "schoolTransactionManager")
    public void deleteClass(Long id) {
        classRepository.deleteById(id);
    }
}