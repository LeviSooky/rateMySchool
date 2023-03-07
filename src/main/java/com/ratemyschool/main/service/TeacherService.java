package com.ratemyschool.main.service;

import com.google.cloud.language.v1.AnalyzeSentimentResponse;
import com.google.cloud.language.v1.Document;
import com.google.cloud.language.v1.LanguageServiceClient;
import com.google.cloud.language.v1.Sentiment;
import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.model.*;
import com.ratemyschool.main.repo.TeacherRepository;
import com.ratemyschool.main.repo.TeacherReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.*;

import static com.ratemyschool.main.enums.RMSConstants.*;

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

    public List<TeacherData> getTeachers(Pageable pageable) {

        return teacherRepository.findAllByStatusEquals(ACTIVE, pageable)
                .toList();
    }
    public TeacherData getTeacherById(UUID id) {
        return teacherRepository.findById(id).orElseThrow(RuntimeException::new);
    }

    public List<TeacherData> getTeachersBySchoolId(UUID schoolId, Pageable pageable) {
        return teacherRepository.findAllBySchoolId(schoolId, pageable).toList();
    }

    public AddReviewResponse addReview(UUID teacherId, TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
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
        byte stars = sentimentService.calculateStars(score);
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
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RuntimeException::new);
        teacher.setStatus(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED);
        teacherRepository.save(teacher);

    }

    private TeacherReviewData save(TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(review.getTeacher().getId())
                .orElseThrow(() -> new RuntimeException("School not found."));
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
                .orElseThrow(() -> new RuntimeException("Review not found."));
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

    public boolean doesTeacherExists(UUID teacherId) {
        return teacherRepository.existsByIdAndStatus(teacherId, ACTIVE);
    }

    public TeacherData update(TeacherData teacher) {
        return teacherRepository.saveAndFlush(teacher); // TODO
    }

    public PageResult<TeacherData, Teacher> findAllActiveBy(String keyword, Pageable pageable) {
        return new PageResult<>(teacherRepository.findAllActiveBy(keyword, pageable));
    }

    public PageResult<TeacherData, Teacher> findAllActive(Pageable pageable) {
        return new PageResult<>(teacherRepository.findAllActive(pageable));
    }

    public Teacher findBy(UUID id) {
        return teacherRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("teacher not found."))
                .toDomainModel();
    }
}
