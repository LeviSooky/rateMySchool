package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.School;
import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.SchoolData;
import com.ratemyschool.main.model.TeacherData;
import com.ratemyschool.main.repo.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository repository;

    public void addTeacherToSchool(UUID schoolId, TeacherData teacher) {
        teacher.setStatus(RMSConstants.PENDING);
        teacher.setCreationDate(LocalDateTime.now());
        teacher.setId(UUID.randomUUID());
        SchoolData schoolData = repository.findById(schoolId).orElseThrow(RuntimeException::new);
        schoolData.addTeacher(teacher);
        repository.save(schoolData);
    }

    public void addSchool(SchoolData schoolData) {
        schoolData.setId(UUID.randomUUID());
        schoolData.setStatus(RMSConstants.PENDING);
        repository.save(schoolData);
    }

    public List<School> findAllBy(String keyword, Pageable pageable) {
        return repository.findAllBy(keyword, pageable)
                .getContent()
                .stream()
                .map(SchoolData::toDomainModel)
                .collect(Collectors.toList());
    }

    public School create(School school) {
        SchoolData schoolData;
        if (Objects.nonNull(school.getId())) {
            schoolData = repository.findById(school.getId())
                    .orElseThrow(() -> new IllegalStateException("school not found"));
        } else {
            schoolData = new SchoolData();
        }
        schoolData.create(school);
        return repository.save(schoolData).toDomainModel();
    }

    public School findBy(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RuntimeException("school not found"))
                .toDomainModel();
    }
}
