package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.service.SchoolReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Controller
@RequestMapping("api/review/school")
public class SchoolReviewController {

    private final SchoolReviewService service;

    @GetMapping("/{schoolId}")
    public ResponseEntity<List<SchoolReview>> findAll(@PathVariable UUID schoolId, Pageable pageable) {
        return service.findAllActiveBy(schoolId, pageable).buildResponse();
    }
}
