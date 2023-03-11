package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.model.*;
import com.ratemyschool.main.repo.TeacherRepository;
import com.ratemyschool.main.repo.TeacherReviewRepository;
import com.ratemyschool.main.exception.RmsRuntimeException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ratemyschool.main.enums.EntityStatus.*;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final TranslateService translateService;
    private final SentimentService sentimentService;
    private final TeacherReviewRepository teacherReviewRepository;

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

    public AddReviewResponse addReview(UUID teacherId, TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RmsRuntimeException::new);
        review.setTeacher(teacher);
        ResponseEntity<DeeplResponse> deeplResponse = translateService.getDeeplApiCallResponse(review.getContent());
        if (!deeplResponse.getStatusCode().equals(HttpStatus.OK)) {
            review.setStatus(EntityStatus.PENDING);
            save(review);
            return AddReviewResponse.builder().status(TRANSLATION_FAILED).build();
        }
        String reviewStatus = Objects.requireNonNull(deeplResponse.getBody()).getFullReviewInEnglish();
        review.setContentInEnglish(reviewStatus);
        float score = sentimentService.calculateSentimentScore(reviewStatus);
        review.setSentimentScore(score);
        if (score == Float.MIN_VALUE) {
            review.setStatus(EntityStatus.PENDING);
            save(review);
            return AddReviewResponse.builder().status(SENTIMENT_FAILED).build();
        }
        int stars = sentimentService.calculateStars(score);
        if (stars == -1) {
            return AddReviewResponse.builder().status(NOT_ACCEPTABLE).build();
        }
        review.setStars(stars);
        review.setStatus(stars > 0 ? EntityStatus.ACTIVE : EntityStatus.PENDING);
        save(review);
        return stars > 0 ?
                AddReviewResponse.builder().stars(stars).status(ACTIVE).build() :
                AddReviewResponse.builder().status(PENDING).build();
    }

    public void activateTeacherById(UUID teacherId, Boolean shouldActivate) {
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RmsRuntimeException::new);
        teacher.setStatus(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED);
        teacherRepository.save(teacher);

    }

    private TeacherReviewData save(TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(review.getTeacher().getId())
                .orElseThrow(() -> new RmsRuntimeException("School not found."));
        Long reviewCounter = teacherReviewRepository.countAllByTeacherIdAndStatus(teacher.getId(), EntityStatus.ACTIVE);
        if (Objects.isNull(review.getId())) {
            if (EntityStatus.ACTIVE.equals(review.getStatus())) {
                teacher.setAvgRating((teacher.getAvgRating() * reviewCounter + review.getStars()) / reviewCounter + 1);
                teacherRepository.save(teacher);
                return teacherReviewRepository.save(review);
            }
            return teacherReviewRepository.save(review);
        }
        TeacherReviewData previous = teacherReviewRepository.findById(review.getId())
                .orElseThrow(() -> new RmsRuntimeException("Review not found."));
        if (previous.getStatus().equals(review.getStatus())) {
            return teacherReviewRepository.save(review);
        }
        if (EntityStatus.ACTIVE.equals(previous.getStatus())) {
            if (reviewCounter > 1) {
                teacher.setAvgRating((teacher.getAvgRating() * reviewCounter - review.getStars()) / reviewCounter - 1);
            } else {
                teacher.setAvgRating(0F);
            }
        }
        if (EntityStatus.ACTIVE.equals(review.getStatus())) {
            teacher.setAvgRating((teacher.getAvgRating() * reviewCounter + review.getStars()) / reviewCounter + 1);
        }
        teacherRepository.save(teacher);
        return teacherReviewRepository.save(review);
    }

//    public boolean doesTeacherExists(UUID teacherId) {
//       // return teacherRepository.existsByIdAndStatus(teacherId, ACTIVE);
//    }

    public TeacherData update(TeacherData teacher) {
        return teacherRepository.saveAndFlush(teacher); // TODO
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
}
