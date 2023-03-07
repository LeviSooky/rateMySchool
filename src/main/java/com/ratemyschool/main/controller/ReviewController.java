package com.ratemyschool.main.controller;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.TeacherReviewData;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/review/teacher")
public class ReviewController { //TODO rename me

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<AddReviewResponse> addReview(@RequestParam UUID teacherId, @RequestParam String review) {
        TeacherReviewData newReview = new TeacherReviewData();
        newReview.setContent(review);
        AddReviewResponse result;
        try {
            result = teacherService.addReview(teacherId, newReview);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
        if(result.getStatus() == RMSConstants.NOT_ACCEPTABLE) {
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
