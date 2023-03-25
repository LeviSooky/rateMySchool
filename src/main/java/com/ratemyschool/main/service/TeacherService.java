package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.PageResult;
import com.ratemyschool.main.model.SchoolData;
import com.ratemyschool.main.model.TeacherData;
import com.ratemyschool.main.repo.SchoolRepository;
import com.ratemyschool.main.repo.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final SchoolRepository schoolRepository;

    public void addTeacher(TeacherData teacher) {
        teacher.setStatus(EntityStatus.ACTIVE);
        teacher.setId(UUID.randomUUID());
        teacherRepository.save(teacher);
    }
    public void deleteTeacher(TeacherData teacher) {
        teacher.setStatus(EntityStatus.DELETED);
        teacherRepository.save(teacher);
    }

    public TeacherData getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(RmsRuntimeException::new);
    }

    public List<TeacherData> getTeachersBySchoolId(UUID schoolId, Pageable pageable) {
        return teacherRepository.findAllBySchoolId(schoolId, pageable).toList();
    }

    public void activateTeacherById(UUID teacherId, Boolean shouldActivate) {
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RmsRuntimeException::new);
        teacher.setStatus(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED);
        teacherRepository.save(teacher);

    }

    public PageResult<TeacherData, Teacher> findAllActiveBy(String keyword, Pageable pageable) {
        return new PageResult<>(teacherRepository.findAllActiveBy(keyword, pageable));
    }

    public PageResult<TeacherData, Teacher> findAllBy(String keyword, Pageable pageable) {
        return new PageResult<>(teacherRepository.findAllBy(keyword, pageable));
    }

    public PageResult<TeacherData, Teacher> findAllActive(Pageable pageable) {
        return new PageResult<>(teacherRepository.findAllActive(pageable));
    }

    public PageResult<TeacherData, Teacher> findAll(Pageable pageable) {
        return new PageResult<>(teacherRepository.findAll(pageable));
    }

    public Teacher findBy(UUID id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RmsRuntimeException("teacher not found."))
                .toDomainModel();
    }

    public Teacher save(Teacher teacher, UUID schoolId) {
        if (Objects.nonNull(teacher.getId())) {
            throw new RmsRuntimeException("new teacher should not have id.");
        }
        SchoolData schoolData = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RmsRuntimeException("School not found."));
        TeacherData toSave = new TeacherData();
        toSave.setSchool(schoolData);
        toSave.setAvgRating(0f);
        toSave.setName(teacher.getName());
        toSave.setIsMale(teacher.getIsMale());
        toSave.setStatus(EntityStatus.PENDING);
        return teacherRepository.save(toSave).toDomainModel();
    }

    public Teacher edit(Teacher teacher, UUID schoolId) {
        TeacherData teacherData = teacherRepository.findById(teacher.getId())
                .orElseThrow(() -> new RmsRuntimeException("teacher not found."));
        SchoolData schoolData = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new RmsRuntimeException("School not found."));
        teacherData.setSchool(schoolData);
        teacherData.setName(teacher.getName());
        teacherData.setIsMale(teacher.getIsMale());
        return teacherRepository.save(teacherData).toDomainModel();
    }
}
