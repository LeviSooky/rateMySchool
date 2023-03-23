package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.School;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.model.PageResult;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.SchoolData;
import com.ratemyschool.main.model.TeacherData;
import com.ratemyschool.main.repo.CityDataRepository;
import com.ratemyschool.main.repo.SchoolRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SchoolService {
    private final SchoolRepository repository;
    private final CityDataRepository cityRepository;

    public void addTeacherToSchool(UUID schoolId, TeacherData teacher) {
        teacher.setStatus(EntityStatus.PENDING);
        teacher.setCreationDate(LocalDateTime.now());
        SchoolData schoolData = repository.findById(schoolId).orElseThrow(RmsRuntimeException::new);
        schoolData.addTeacher(teacher);
        repository.save(schoolData);
    }

    public void addSchool(SchoolData schoolData) {
        schoolData.setStatus(EntityStatus.PENDING);
        repository.save(schoolData);
    }

    public PageResult<SchoolData, School> findAllBy(String keyword, Pageable pageable) {
        return new PageResult<>(repository.findAllActiveBy(keyword, pageable));
    }

    public PageResult<SchoolData, School> findAll(Pageable pageable) {
        return new PageResult<>(repository.findAllActive(pageable));
    }

    public School create(School school) {
        SchoolData schoolData;
        if (Objects.nonNull(school.getId())) {
            schoolData = repository.findById(school.getId())
                    .orElseThrow(() -> new IllegalStateException("school not found"));
        } else {
            schoolData = new SchoolData();
            schoolData.setStatus(EntityStatus.PENDING);
            schoolData.setAvgRating(0f);
        }
        if (Objects.nonNull(school.getCity())) {
            cityRepository.findById(school.getCity().getId()).orElseThrow(() -> new RmsRuntimeException("city not found"));
        }
        schoolData.create(school);
        return repository.save(schoolData).toDomainModel();
    }

    public School findBy(UUID id) {
        return repository.findById(id)
                .orElseThrow(() -> new RmsRuntimeException("school not found"))
                .toDomainModel();
    }

    public void moderate(UUID schoolId, Boolean shouldActivate) {
        SchoolData school = repository.findById(schoolId)
                .orElseThrow(() -> new RmsRuntimeException("school not found"));
        school.setStatus(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED);
        repository.save(school);
    }

    public PageResult<SchoolData, School> moderatorFindAllBy(String keyword, Pageable pageable) {
        if (Strings.isBlank(keyword)) {
            return new PageResult<>(repository.findAll(pageable));
        }
        return new PageResult<>(repository.findAllBy(keyword, pageable));
    }
}
