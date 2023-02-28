package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.service.TeacherReviewService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController("api/review/teacher")
@RequiredArgsConstructor
@Log4j2
public class TeacherReviewController {
    private final TeacherReviewService service;


    @GetMapping("/{teacherId}")
    public ResponseEntity<List<TeacherReview>> findAllActiveBy(@PathVariable UUID teacherId, Pageable pageable) {
        log.info("REST request for active teacher reviews by teacher id: {}", teacherId);
        return service.findAllActiveBy(teacherId, pageable).buildResponse();
    }
}
