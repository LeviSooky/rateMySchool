package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.AddReviewResponse;
import com.ratemyschool.main.model.SchoolReviewData;
import com.ratemyschool.main.service.SchoolReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.NotImplementedException;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@Log4j2
@RequestMapping("api/review/school")
public class SchoolReviewController {

    private final SchoolReviewService service;

    @GetMapping("/{schoolId}")
    public ResponseEntity<List<SchoolReview>> findAll(@PathVariable UUID schoolId, Pageable pageable) {
        log.info("REST request to find all school reviews by id: {}", schoolId);
        return service.findAllActiveBy(schoolId, pageable).buildResponse();
    }

    @GetMapping
    public ResponseEntity<AddReviewResponse> create(@RequestParam UUID schoolId, @RequestParam String review) {
        log.info("REST request to crate school review by schoolId: {}, review: {}", schoolId, review);
        SchoolReviewData toSave = new SchoolReviewData();
        toSave.setContent(review);
        AddReviewResponse response;
        try {
            response = service.create(schoolId, toSave);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "School Not Found");
        }
        if(response.getStatus() == RMSConstants.NOT_ACCEPTABLE) {
            return new ResponseEntity<>(response, HttpStatus.NOT_ACCEPTABLE);
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
