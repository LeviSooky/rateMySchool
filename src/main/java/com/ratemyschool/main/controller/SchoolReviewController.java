package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.SchoolReviewData;
import com.ratemyschool.main.service.SchoolReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Log4j2
@Validated
@RequestMapping("api/review/school")
public class SchoolReviewController {

    private final SchoolReviewService service;

    @GetMapping("/{schoolId}")
    public ResponseEntity<List<SchoolReview>> findAll(@PathVariable UUID schoolId, Pageable pageable) {
        log.info("REST request to find all school reviews by id: {}", schoolId);
        return service.findAllActiveBy(schoolId, pageable).buildResponse();
    }

    @GetMapping
    public ResponseEntity<AddReviewResponse> create(@RequestParam UUID schoolId,
                                                    @NotBlank @Size(min = 10) @RequestParam String review) {
        log.info("REST request to crate school review by schoolId: {}, review: {}", schoolId, review);
        SchoolReviewData toSave = new SchoolReviewData();
        toSave.setContent(review);
        AddReviewResponse response;
        try {
            response = service.create(schoolId, toSave);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "School Not Found");
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @GetMapping("/modify/stars/{reviewId}")
    public ResponseEntity<Void> modifyStars(@PathVariable UUID reviewId, @RequestParam Integer stars) {
        log.info("REST request to modify review's stars by reviewId: {}, stars: {}", reviewId, stars);
        service.modifyStars(reviewId, stars);
        return ResponseEntity.ok().build();
    }
}
