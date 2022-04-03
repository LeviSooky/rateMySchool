package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.repo.TeacherRepository;
import com.sun.istack.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TeacherService {

    private final String SENTIMENT_REST_URL = "TODO";
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

    public String addReview(UUID teacherId, Review review) {

        RestTemplate restTemplate = new RestTemplate();
        String reviewStatus = restTemplate.getForObject(SENTIMENT_REST_URL, String.class, review);
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
        if("NOT_OK".equals(reviewStatus)) {
            return reviewStatus;
        }
        review.setId(UUID.randomUUID());
        review.setStatusFlag(reviewStatus.equals("OK") ? RMSConstants.ACTIVE : RMSConstants.PENDING);
        teacher.addReview(review);
        teacherRepository.save(teacher);
        return reviewStatus;
    }

    public void activateTeacherById(UUID teacherId, Boolean shouldActivate) {
        Teacher teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
        teacher.setStatus(shouldActivate ? RMSConstants.ACTIVE : RMSConstants.DELETED);
        teacherRepository.save(teacher);

    }
}
