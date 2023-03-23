package com.ratemyschool.main.service;

import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.DeeplResponse;
import com.ratemyschool.main.model.PageResult;
import com.ratemyschool.main.model.SchoolData;
import com.ratemyschool.main.model.SchoolReviewData;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.repo.SchoolRepository;
import com.ratemyschool.main.repo.SchoolReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

import static com.ratemyschool.main.enums.EntityStatus.ACTIVE;
import static com.ratemyschool.main.enums.EntityStatus.NOT_ACCEPTABLE;
import static com.ratemyschool.main.enums.EntityStatus.PENDING;
import static com.ratemyschool.main.enums.EntityStatus.SENTIMENT_FAILED;
import static com.ratemyschool.main.enums.EntityStatus.TRANSLATION_FAILED;

@RequiredArgsConstructor
@Service
public class SchoolReviewService {

    private final SchoolReviewRepository reviewRepository;
    private final SchoolRepository schoolRepository;
    private final TranslateService translateService;
    private final SentimentService sentimentService;

    public PageResult<SchoolReviewData, SchoolReview> findAllActiveBy(UUID id, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllActiveBy(id, pageable));
    }

    public PageResult<SchoolReviewData, SchoolReview> findAllBy(UUID id, Pageable pageable) {
        return new PageResult<>(reviewRepository.findAllBy(id, pageable));
    }

    public AddReviewResponse create(UUID schoolId, SchoolReviewData review) {
        SchoolData school = schoolRepository.findById(schoolId).orElseThrow(RmsRuntimeException::new);
        review.setSchool(school);
        ResponseEntity<DeeplResponse> deeplResponse = translateService.getDeeplApiCallResponse(review.getContent());
        if (!deeplResponse.getStatusCode().equals(HttpStatus.OK)) {
            review.setStatus(EntityStatus.PENDING);
            review = save(review);
            return AddReviewResponse.builder().status(TRANSLATION_FAILED).id(review.getId()).build();
        }
        String reviewStatus = Objects.requireNonNull(deeplResponse.getBody()).getFullReviewInEnglish();
        review.setContentInEnglish(reviewStatus);
        float score = sentimentService.calculateSentimentScore(reviewStatus);
        review.setSentimentScore(score);
        if (score == Float.MIN_VALUE) {
            review.setStatus(EntityStatus.PENDING);
            review = save(review);
            return AddReviewResponse.builder().status(SENTIMENT_FAILED).id(review.getId()).build();
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
                AddReviewResponse.builder().status(PENDING).build();
    }

    private SchoolReviewData save(SchoolReviewData review) {
        SchoolData school = schoolRepository.findById(review.getSchool().getId())
                .orElseThrow(() -> new RmsRuntimeException("School not found."));
        Long reviewCounter = reviewRepository.countAllBySchoolIdAndStatus(school.getId(), EntityStatus.ACTIVE);
        if (Objects.isNull(review.getId())) {
            if (EntityStatus.ACTIVE.equals(review.getStatus())) {
                school.setAvgRating((school.getAvgRating() * reviewCounter + review.getStars()) / (reviewCounter + 1));
                schoolRepository.save(school);
                return reviewRepository.save(review);
            }
            return reviewRepository.save(review);
        }
        SchoolReviewData previous = reviewRepository.findById(review.getId())
                .orElseThrow(() -> new RmsRuntimeException("Review not found."));
        if (previous.getStatus().equals(review.getStatus())) {
            if (!Objects.equals(review.getStars(), previous.getStars())) {
                school.setAvgRating(
                        (school.getAvgRating() * reviewCounter - (previous.getStars() - review.getStars()))
                                / reviewCounter);
                schoolRepository.save(school);
            }
            return reviewRepository.save(review);
        }
        if (EntityStatus.ACTIVE.equals(previous.getStatus())) {
            if (reviewCounter > 1) {
                school.setAvgRating((school.getAvgRating() * reviewCounter - review.getStars()) / (reviewCounter - 1));
            } else {
             school.setAvgRating(0F);
            }
        }
        if (EntityStatus.ACTIVE.equals(review.getStatus())) {
            school.setAvgRating((school.getAvgRating() * reviewCounter + review.getStars()) / (reviewCounter + 1));
        }
        schoolRepository.save(school);
        return reviewRepository.save(review);
    }

    public void moderate(UUID reviewId, Boolean shouldActivate) {
        SchoolReviewData review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RmsRuntimeException("school review not found."));
        SchoolReviewData toSave = review.toBuilder().status(shouldActivate ? EntityStatus.ACTIVE : EntityStatus.DELETED)
                .build();
        save(toSave);
    }

    public void modifyStars(UUID reviewId, Integer stars) {
        SchoolReviewData review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RmsRuntimeException("school review not found."));
        SchoolReviewData toSave = review.toBuilder().stars(stars).build(); // very important because it uses the cached entity from the above line
        save(toSave);
    }
}
