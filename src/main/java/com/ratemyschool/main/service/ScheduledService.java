package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.DeeplResponse;
import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.repo.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;
import java.util.stream.Collectors;

import static com.ratemyschool.main.enums.RMSConstants.ACTIVE;
import static com.ratemyschool.main.enums.RMSConstants.PENDING;

@EnableAsync
@RequiredArgsConstructor
public class ScheduledService {
    private final ReviewService reviewService;
    private final TeacherService teacherService;
    @Async
    @Scheduled(fixedDelay = 604_800_000)
    public void reRunFailedSentiments() throws InterruptedException { //TODO check (async + ) + other methods
        List<Review> failedReviews = reviewService.getFailedReviews();
        List<Review> result = failedReviews.stream().peek(this::performOperations).collect(Collectors.toList());
        reviewService.saveAll(result);
    }

    public void performOperations(Review review) {
        if(review.getStatusFlag() == RMSConstants.TRANSLATION_FAILED) {
            ResponseEntity<DeeplResponse> deeplApiCallResponse = teacherService.getDeeplApiCallResponse(review);
            if(!deeplApiCallResponse.getStatusCode().equals(HttpStatus.OK)) {
                return;
            }
            String fullReviewInEnglish = deeplApiCallResponse.getBody().getFullReviewInEnglish();
            review.setContentInEnglish(fullReviewInEnglish);
            review.setStatusFlag(RMSConstants.SENTIMENT_FAILED);
        }
        if(review.getStatusFlag() == RMSConstants.SENTIMENT_FAILED) {
            float score = teacherService.calculateSentimentScore(review.getContentInEnglish());
            if(score == Float.MIN_VALUE) {
                return;
            }
            byte stars = teacherService.calculateStars(score);
            if(stars == -1) {
                review.setStatusFlag(RMSConstants.DELETED);
                return;
            }
            review.setStars(stars);
            review.setSentimentScore(score);
            review.setStatusFlag(stars > 0 ? ACTIVE : PENDING);
        }
    }

}