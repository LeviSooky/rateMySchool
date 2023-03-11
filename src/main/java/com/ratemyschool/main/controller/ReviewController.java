package com.ratemyschool.main.controller;

import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.TeacherReviewData;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("/api/review/teacher")
public class ReviewController { //TODO rename me

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<AddReviewResponse> addReview(@RequestParam UUID teacherId,
                                                       @NotBlank @Size(min = 10) @RequestParam String review) {
        TeacherReviewData newReview = new TeacherReviewData();
        newReview.setContent(review);
        AddReviewResponse result;
        try {
            result = teacherService.addReview(teacherId, newReview);
        } catch (RmsRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
        if(result.getStatus() == EntityStatus.NOT_ACCEPTABLE) {
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } //TODO change
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
