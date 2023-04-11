package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.entity.TeacherData;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Log4j2
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping(path = "/search/{keyword}")
    public ResponseEntity<List<Teacher>> findAllBy(@PathVariable String keyword, Pageable pageable) {
        log.info("REST request for teacher search by {}", keyword);
        return teacherService.findAllActiveBy(keyword, pageable).buildResponse();
    }

    @GetMapping(path = "/search")
    public ResponseEntity<List<Teacher>> findAllBy(Pageable pageable) {
        log.info("REST request for teacher search");
        return teacherService.findAllActive(pageable).buildResponse();
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<Teacher> findBy(@PathVariable UUID id) {
        log.info("REST request to get teacher by id: {}", id);
        Teacher result = teacherService.findBy(id);
        return ResponseEntity.ok(result);
    }

    @PostMapping(path = "/add")
    public void addTeacher(@RequestBody TeacherData teacher) {
        teacherService.addTeacher(teacher);
    }

    @PostMapping(path = "/add/{schoolId}")
    public ResponseEntity<Teacher> create(@RequestBody Teacher teacher, @PathVariable UUID schoolId) {
        log.info("REST request to create teacher: {}", teacher);
        Teacher saved = teacherService.save(teacher, schoolId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
