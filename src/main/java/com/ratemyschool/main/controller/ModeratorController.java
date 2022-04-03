package com.ratemyschool.main.controller;

import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.service.ReviewService;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController("/moderator")
@RequiredArgsConstructor
public class ModeratorController {
    private final ReviewService reviewService;
    private final TeacherService teacherService;

    @GetMapping("/reviews/pending")
    public ResponseEntity<List<Review>> getPendingReviews() {
        List<Review> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/moderate/{reviewId}")
    public ResponseEntity<String> activateReview(@PathVariable UUID reviewId, @RequestParam Boolean shouldActivate) {
        try {
            reviewService.activateReviewById(reviewId, shouldActivate);
        } catch (RuntimeException e) {
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "review not found"
            );
        }
        return ResponseEntity.ok("success");
    }

    @GetMapping("/teachers/moderate/{teacherId}")
    public ResponseEntity<String> moderateTeacher(@PathVariable UUID teacherId, @RequestParam Boolean shouldActivate) {
        try {
            teacherService.activateTeacherById(teacherId, shouldActivate);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "teacher not found");
        }
        return ResponseEntity.ok("success");
    }
}
