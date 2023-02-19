package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.School;
import com.ratemyschool.main.service.SchoolService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Log4j2
@RestController
@RequestMapping("api/school")
public class SchoolController {

    private final SchoolService service;

    @GetMapping("/search/{keyword}")
    public ResponseEntity<List<School>> findAllBy(@PathVariable String keyword, Pageable pageable) {
        log.info("REST request for school search by: {}", keyword);
        return service.findAllBy(keyword, pageable).buildResponse();
    }

    @GetMapping("/search")
    public ResponseEntity<List<School>> findAll(Pageable pageable) {
        log.info("REST request for all school");
        return service.findAll(pageable).buildResponse();
//        ResponseEntity<List<School>> response = ResponseEntity.ok(result.getContent());
//        response.getHeaders().set("totalPages", result.getTotalPages().toString());
//        response.getHeaders().set("totalElements", result.getTotalElements().toString());
//        return response;
    }

    @PostMapping
    public ResponseEntity<School> create(@Valid School school) {
        log.info("REST request to create school {}", school);
        School saved = service.create(school); //TODO status handling
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PutMapping
    public ResponseEntity<School> update(@Valid School school) {
        log.info("REST request to update school {}", school);
        School saved = service.create(school);
        return ResponseEntity.ok(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<School> findBy(@PathVariable UUID id) {
        log.info("REST request to find school by id {}", id);
        School school = service.findBy(id);
        return ResponseEntity.ok(school);
    }

}
