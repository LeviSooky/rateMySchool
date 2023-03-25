package com.ratemyschool.main.service;

import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;

@EnableAsync
@RequiredArgsConstructor //TODO rethink
public class ScheduledService {
    private final TeacherReviewService reviewService;

    @Scheduled
    @Async
    public void deleteDeletedReviews() {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime fromDate = currentTime.minusDays(7);
        reviewService.deleteDeletedComments(currentTime, fromDate);

    }

}