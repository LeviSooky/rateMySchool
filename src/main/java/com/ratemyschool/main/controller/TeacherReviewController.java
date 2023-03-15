package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.TeacherReviewData;
import com.ratemyschool.main.service.TeacherReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/review/teacher")
@RequiredArgsConstructor
@Validated
@Log4j2
public class TeacherReviewController {
    private final TeacherReviewService service;


    @GetMapping("/{teacherId}")
    public ResponseEntity<List<TeacherReview>> findAllActiveBy(@PathVariable UUID teacherId, Pageable pageable) {
        log.info("REST request for active teacher reviews by teacher id: {}", teacherId);
        return service.findAllActiveBy(teacherId, pageable).buildResponse();
    }

    @GetMapping("/modify/stars/{reviewId}")
    public ResponseEntity<Void> modifyStars(@PathVariable UUID reviewId, @RequestParam Integer stars) {
        log.info("REST request to modify review's stars by reviewId: {}, stars: {}", reviewId, stars);
        service.modifyStars(reviewId, stars);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<AddReviewResponse> addReview(@RequestParam UUID teacherId,
                                                       @NotBlank @Size(min = 10) @RequestParam String review) {
        TeacherReviewData newReview = new TeacherReviewData();
        newReview.setContent(review);
        AddReviewResponse result;
        try {
            result = service.addReview(teacherId, newReview);
        } catch (RmsRuntimeException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
        if(result.getStatus() == EntityStatus.NOT_ACCEPTABLE) {
            return new ResponseEntity<>(result, HttpStatus.NOT_ACCEPTABLE);
        } //TODO change
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
