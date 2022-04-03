package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.School;
import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.repo.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository schoolRepository;

    public void addTeacherToSchool(UUID schoolId, Teacher teacher) {
        teacher.setStatus(RMSConstants.PENDING);
        teacher.setCreationDate(LocalDateTime.now());
        teacher.setId(UUID.randomUUID());
        School school = schoolRepository.findById(schoolId).orElseThrow(RuntimeException::new);
        school.addTeacher(teacher);
        schoolRepository.save(school);
    }

    public void addSchool(School school) {
        school.setId(UUID.randomUUID());
        school.setStatus(RMSConstants.PENDING);
        schoolRepository.save(school);
    }
}
