package com.ratemyschool.main.controller;

import com.ratemyschool.main.model.Review;
import com.ratemyschool.main.service.ReviewService;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review")
public class ReviewController {

    private final ReviewService reviewService;
    private final TeacherService teacherService;

    @PutMapping("/add")
    public ResponseEntity<String> addReview(@RequestParam UUID teacherId, @RequestParam String review) {
        Review newReview = new Review();
        newReview.setCreationDate(LocalDateTime.now());
        newReview.setContent(review);
        boolean isSuccess;
        try {
           isSuccess = teacherService.addReview(teacherId, newReview);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
        if(!isSuccess) {
            return new ResponseEntity<>("The review's content is unacceptable.", HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>("success", HttpStatus.OK);
    }



}
