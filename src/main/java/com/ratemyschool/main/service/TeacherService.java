package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.RMSTeacher;
import com.ratemyschool.main.repo.RMSTeacherRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final RMSTeacherRepository teacherRepository;

    //TODO maybe remove status
    public void addTeacher(RMSTeacher teacher) {
        teacher.setStatus(RMSConstants.ACTIVE);
        teacher.setId(UUID.randomUUID());
        teacherRepository.save(teacher);
    }
    public void deleteTeacher(RMSTeacher teacher) {
        teacher.setStatus(RMSConstants.DELETED);
        teacherRepository.save(teacher);
    }

    public List<RMSTeacher> getTeachers(int pageNo, int pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        return teacherRepository.findAllByStatusEquals(RMSConstants.ACTIVE, paging).toList();
    }
    public RMSTeacher getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(RuntimeException::new);
    }

}
