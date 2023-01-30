package com.ratemyschool.main.controller;

import com.ratemyschool.main.model.ReviewData;
import com.ratemyschool.main.model.TeacherData;
import com.ratemyschool.main.service.ReviewService;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
    public ResponseEntity<List<ReviewData>> getPendingReviews() {
        List<ReviewData> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/reviews/moderate/{reviewId}")
    public ResponseEntity<Void> activate(@PathVariable UUID reviewId, @RequestParam Boolean shouldActivate) {
        try {
            reviewService.activateReviewById(reviewId, shouldActivate);
        } catch (RuntimeException e) {
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "review not found"
            );
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
    @PostMapping("teachers/update")
    public ResponseEntity<TeacherData> updateTeacher(@RequestBody TeacherData teacher) {
       if(teacherService.doesTeacherExists(teacher.getId())) {
           return ResponseEntity.ok(teacherService.update(teacher));
       }
        throw  new ResponseStatusException(
                HttpStatus.NOT_FOUND, "teacher not found"
        );
    }

    @GetMapping("/teachers/moderate/{teacherId}")
    public ResponseEntity<Void> moderateTeacher(@PathVariable UUID teacherId, @RequestParam Boolean shouldActivate) {
        try {
            teacherService.activateTeacherById(teacherId, shouldActivate);
        } catch (RuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "teacher not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }


}
