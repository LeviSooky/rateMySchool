package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.School;
import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.entity.TeacherReviewData;
import com.ratemyschool.main.service.SchoolReviewService;
import com.ratemyschool.main.service.SchoolService;
import com.ratemyschool.main.service.TeacherReviewService;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("moderator")
public class ModeratorController {
    private final TeacherReviewService reviewService;
    private final TeacherService teacherService;
    private final SchoolReviewService schoolReviewService;
    private final SchoolService schoolService;

    @GetMapping("/reviews/pending")
    public ResponseEntity<List<TeacherReviewData>> getPendingReviews() {
        List<TeacherReviewData> reviews = reviewService.getPendingReviews();
        return ResponseEntity.ok(reviews);
    }

    @GetMapping("/teachers/reviews/moderate/{reviewId}")
    public ResponseEntity<Void> moderate(@PathVariable UUID reviewId, @RequestParam Boolean shouldActivate) {
        try {
            reviewService.moderateReview(reviewId, shouldActivate);
        } catch (RmsRuntimeException e) {
            throw  new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "review not found"
            );
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

    @GetMapping("/teachers/reviews/{teacherId}")
    public ResponseEntity<List<TeacherReview>> findTeacherReviewsBy(@PathVariable UUID teacherId, Pageable pageable) {
        log.debug("REST request for search teacher reviews by: {}", teacherId);
        return reviewService.findAllBy(teacherId, pageable).buildResponse();
    }

    @GetMapping("/schools/reviews/{schoolId}")
    public ResponseEntity<List<SchoolReview>> findSchoolReviewsBy(@PathVariable UUID schoolId, Pageable pageable) {
        log.debug("REST request for search school reviews by: {}", schoolId);
        return schoolReviewService.findAllBy(schoolId, pageable).buildResponse();
    }

    @GetMapping("/schools/reviews/moderate/{reviewId}")
    public ResponseEntity<Void> moderateSchoolReview(@PathVariable UUID reviewId,
                                                     @RequestParam Boolean shouldActivate) {
        schoolReviewService.moderate(reviewId, shouldActivate);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/schools/moderate/{schoolId}")
    public ResponseEntity<Void> moderateSchool(@PathVariable UUID schoolId,
                                               @RequestParam Boolean shouldActivate) {
        schoolService.moderate(schoolId, shouldActivate);
        return ResponseEntity.accepted().build();
    }

    @GetMapping("/schools/search/{keyword}")
    public ResponseEntity<List<School>> findAllBy(@PathVariable String keyword, Pageable pageable) {
        log.info("REST request for school search by: {}", keyword);
        return schoolService.moderatorFindAllBy(keyword, pageable).buildResponse();
    }

    @GetMapping("/schools/search")
    public ResponseEntity<List<School>> findAllBy(Pageable pageable) {
        log.info("REST request for schools");
        return schoolService.moderatorFindAllBy(null, pageable).buildResponse();
    }

    @GetMapping("/schools/review/{schoolId}")
    public ResponseEntity<List<SchoolReview>> findAll(@PathVariable UUID schoolId, Pageable pageable) {
        log.info("REST request to find all school reviews by id: {}", schoolId);
        return schoolReviewService.findAllBy(schoolId, pageable).buildResponse();
    }

    @PutMapping("/schools")
    public ResponseEntity<School> update(@RequestBody School school) {
        log.info("REST request to update school: {}", school);
        return ResponseEntity.ok(schoolService.create(school));
    }

    @GetMapping(path = "/teachers/search/{keyword}")
    public ResponseEntity<List<Teacher>> findAllTeachersBy(@PathVariable String keyword, Pageable pageable) {
        log.info("REST request for teacher search by {}", keyword);
        return teacherService.findAllBy(keyword, pageable).buildResponse();
    }

    @GetMapping(path = "/teachers/search")
    public ResponseEntity<List<Teacher>> findAllTeachersBy(Pageable pageable) {
        log.info("REST request for teacher search");
        return teacherService.findAll(pageable).buildResponse();
    }

    @GetMapping("/teachers/review/{teacherId}")
    public ResponseEntity<List<TeacherReview>> findAllActiveBy(@PathVariable UUID teacherId, Pageable pageable) {
        log.info("REST request for active teacher reviews by teacher id: {}", teacherId);
        return reviewService.findAllBy(teacherId, pageable).buildResponse();
    }
    @PutMapping("/teachers/edit/{schoolId}")
    public ResponseEntity<Teacher> update(@RequestBody Teacher teacher, @PathVariable UUID schoolId) {
        log.info("REST request to update teacher: {}", teacher);
        Teacher edited = teacherService.edit(teacher, schoolId);
        return ResponseEntity.ok(edited);
    }

    @GetMapping("/teachers/moderate/{teacherId}")
    public ResponseEntity<Void> moderateTeacher(@PathVariable UUID teacherId, @RequestParam Boolean shouldActivate) {
        try {
            teacherService.activateTeacherById(teacherId, shouldActivate);
        } catch (RmsRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "teacher not found");
        }
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }

}
