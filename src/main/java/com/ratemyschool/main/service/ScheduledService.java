package com.ratemyschool.main.service;

import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.DeeplResponse;
import com.ratemyschool.main.model.TeacherReviewData;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static com.ratemyschool.main.enums.RMSConstants.ACTIVE;
import static com.ratemyschool.main.enums.RMSConstants.PENDING;

@EnableAsync
@RequiredArgsConstructor //TODO rethink
public class ScheduledService {
    private final TeacherReviewService reviewService;
    private final TeacherService teacherService;
    private final SentimentService sentimentService;
    private final TranslateService translateService;
//    @Async
//    @Scheduled(fixedDelay = 604_800_000)
//    public void reRunFailedSentiments() throws InterruptedException { //TODO check (async + ) + other methods
//        List<TeacherReviewData> failedReviews = reviewService.getFailedReviews();
//        List<TeacherReviewData> result = failedReviews.stream().peek(this::performOperations).collect(Collectors.toList());
//        reviewService.saveAll(result);
//    }

//    public void performOperations(TeacherReviewData review) {
//        if(review.getStatus() == EntityStatus.TRANSLATION_FAILED) {
//            ResponseEntity<DeeplResponse> deeplApiCallResponse = translateService.getDeeplApiCallResponse(review.getContent());
//            if(!deeplApiCallResponse.getStatusCode().equals(HttpStatus.OK)) {
//                return;
//            }
//            String fullReviewInEnglish = deeplApiCallResponse.getBody().getFullReviewInEnglish();
//            review.setContentInEnglish(fullReviewInEnglish);
//            review.setStatus(EntityStatus.SENTIMENT_FAILED);
//        }
//        if(review.getStatus() == EntityStatus.SENTIMENT_FAILED) {
//            float score = sentimentService.calculateSentimentScore(review.getContentInEnglish());
//            if(score == Float.MIN_VALUE) {
//                return;
//            }
//            byte stars = teacherService.calculateStars(score);
//            if(stars == -1) {
//                review.setStatus(EntityStatus.DELETED);
//                return;
//            }
//            review.setStars(stars);
//            review.setSentimentScore(score);
//            review.setStatus(stars > 0 ? EntityStatus.ACTIVE : EntityStatus.PENDING);
//        }
//    }

    @Scheduled
    @Async
    public void deleteDeletedReviews() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime fromDate = currentTime.minusDays(7);
        reviewService.deleteDeletedComments(currentTime, fromDate);

    }

}