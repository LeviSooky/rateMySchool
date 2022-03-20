package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.repo.TeacherRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepository teacherRepository;
    // private final ModelMapper modelMapper; //TODO move the bean

    public void addTeacher(Teacher teacher) {
        teacher.setStatus(RMSConstants.ACTIVE);
        teacher.setId(UUID.randomUUID());
        teacherRepository.save(teacher);
    }
    public void deleteTeacher(Teacher teacher) {
        teacher.setStatus(RMSConstants.DELETED);
        teacherRepository.save(teacher);
    }

    public List<Teacher> getTeachers(Pageable pageable) {

        return teacherRepository.findAllByStatusEquals(RMSConstants.ACTIVE, pageable)
                //.map(teacher -> modelMapper.map(teacher, TeacherDTO.class))
                .toList();
    }
    public Teacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<Teacher> getTeachersBySchoolId(UUID schoolId, Pageable pageable) {
        return teacherRepository.findAllBySchoolId(schoolId, pageable).toList();
    }

}
