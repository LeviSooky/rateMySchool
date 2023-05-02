package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.dto.AddReviewResponse;
import com.ratemyschool.main.dto.DeeplResponse;
import com.ratemyschool.main.dto.PageResult;
import com.ratemyschool.main.entity.TeacherData;
import com.ratemyschool.main.entity.TeacherReviewData;
import com.ratemyschool.main.repository.TeacherRepository;
import com.ratemyschool.main.repository.TeacherReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.ratemyschool.main.enums.EntityStatus.ACTIVE;
import static com.ratemyschool.main.enums.EntityStatus.NOT_ACCEPTABLE;
import static com.ratemyschool.main.enums.EntityStatus.PENDING;
import static com.ratemyschool.main.enums.EntityStatus.SENTIMENT_FAILED;
import static com.ratemyschool.main.enums.EntityStatus.TRANSLATION_FAILED;

@Service
@RequiredArgsConstructor
public class TeacherReviewService {
    private final TeacherReviewRepository reviewRepository;
    private final TeacherRepository teacherRepository;
    private final TranslateService translateService;
    private final SentimentService sentimentService;

    @Transactional
    public AddReviewResponse addReview(UUID teacherId, TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(teacherId).orElseThrow(RmsRuntimeException::new);
        review.setTeacher(teacher);
        ResponseEntity<DeeplResponse> deeplResponse = translateService.getDeeplApiCallResponse(review.getContent());
        if (!deeplResponse.getStatusCode().equals(HttpStatus.OK)) {
            review.setStatus(EntityStatus.PENDING);
            review = save(review);
            return AddReviewResponse.builder().id(review.getId()).status(TRANSLATION_FAILED).build();
        }
        String reviewStatus = Objects.requireNonNull(deeplResponse.getBody()).getFullReviewInEnglish();
        review.setContentInEnglish(reviewStatus);
        float score = sentimentService.calculateSentimentScore(reviewStatus);
        review.setSentimentScore(score);
        if (score == Float.MIN_VALUE) {
            review.setStatus(EntityStatus.PENDING);
            review = save(review);
            return AddReviewResponse.builder().id(review.getId()).status(SENTIMENT_FAILED).build();
        }
        int stars = sentimentService.calculateStars(score);
        if (stars == -1) {
            return AddReviewResponse.builder().status(NOT_ACCEPTABLE).build();
        }
        review.setStars(stars);
        review.setStatus(stars > 0 ? EntityStatus.ACTIVE : EntityStatus.PENDING);
        review = save(review);
        return stars > 0 ?
                AddReviewResponse.builder().stars(stars).status(ACTIVE).id(review.getId()).build() :
                AddReviewResponse.builder().status(PENDING).id(review.getId()).build();
    }

    private TeacherReviewData save(TeacherReviewData review) {
        TeacherData teacher = teacherRepository.findById(review.getTeacher().getId())
                .orElseThrow(() -> new RmsRuntimeException("School not found."));
        Long reviewCounter = reviewRepository.countAllByTeacherIdAndStatus(teacher.getId(), EntityStatus.ACTIVE);
        if (Objects.isNull(review.getId())) {
            if (EntityStatus.ACTIVE.equals(review.getStatus())) {
                teacher.setAvgRating((teacher.getAvgRating() * reviewCounter + review.getStars()) / (reviewCounter + 1));
                teacherRepository.save(teacher);
                return reviewRepository.save(review);
            }
            return reviewRepository.save(review);
        }
        TeacherReviewData previous = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new RmsRuntimeException("Review not found."));
        if (previous.getStatus().equals(review.getStatus())) {
            if (!Objects.equals(review.getStars(), previous.getStars())) {
                teacher.setAvgRating(
                        (teacher.getAvgRating() * reviewCounter - (previous.getStars() - review.getStars()))
                                / reviewCounter);
                teacherRepository.save(teacher);
            }
            return reviewRepository.save(review);
        }
        if (EntityStatus.ACTIVE.equals(previous.getStatus())) {
            if (reviewCounter > 1) {
                teacher.setAvgRating((teacher.getAvgRating() * reviewCounter - review.getStars()) / (reviewCounter - 1));
            } else {
                teacher.setAvgRating(0F);
            }
        }
        if (EntityStatus.ACTIVE.equals(review.getStatus())) {
            teacher.setAvgRating((teacher.getAvgRating() * reviewCounter + review.getStars()) / (reviewCounter + 1));
        }
        teacherRepository.save(teacher);
        return reviewRepository.save(review);
    }

    public List<TeacherReviewData> getPendingReviews() {
        return reviewRepository.findAllByStatusOrderByCreationDate(EntityStatus.PENDING);
    }

    @Transactional
    public void moderateReview(UUID reviewId, Boolean shouldActivate) {
        TeacherReviewData review = reviewRepository.findById(reviewId)
            .orElseThrow(() -> new RmsRuntimeException("teacher review not found."));
        TeacherReviewData toSave = review.toBuilder().status(shouldActivate ? ACTIVE : EntityStatus.DELETED).build();
        reviewRepository.save(toSave);
    }

    public List<TeacherReviewData> getFailedReviews() {
        return reviewRepository.findAllByStatusIn(List.of(EntityStatus.SENTIMENT_FAILED, EntityStatus.TRANSLATION_FAILED));
    }

    void saveAll(List<TeacherReviewData> result) {
        reviewRepository.saveAll(result);
    }

    void deleteDeletedComments(LocalDateTime current, LocalDateTime from) {
        reviewRepository.deleteAllByLastModifiedBetweenAndStatus(current, from, EntityStatus.DELETED);
    }

    public PageResult<TeacherReviewData, TeacherReview> findAllActiveBy(UUID teacherId, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllActive(teacherId, pageable));
    }

    public PageResult<TeacherReviewData, TeacherReview> findAllBy(UUID teacherId, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllByTeacherId(teacherId, pageable));
    }

    @Transactional
    public void modifyStars(UUID reviewId, Integer stars) {
        TeacherReviewData review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RmsRuntimeException("teacher review not found."));
        TeacherReviewData toSave = review.toBuilder().stars(stars).build(); // very important because it uses the cached entity from the above line
        save(toSave);
    }
}
