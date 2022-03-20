package com.ratemyschool.main.controller;

import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    @GetMapping(path = "/all")
    public ResponseEntity<List<Teacher>> getTeachers(
        @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
        @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize,
        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort) {

        List<Teacher> teacherList = teacherService.getTeachers(pageNo, pageSize, sort);
        return ResponseEntity.ok(teacherList);
    }

    @PostMapping(path = "/add")
    public void addTeacher(@RequestBody Teacher teacher) {
        teacherService.addTeacher(teacher);
    }

    //TODO to admin
    @DeleteMapping(path = "/delete/{teacherId}")
    public ResponseEntity<Boolean> deleteTeacher(@PathVariable UUID teacherId) {
        try {
            Teacher teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacher);
            return ResponseEntity.ok(Boolean.TRUE);
        }
         catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Teacher Not Found", e);
        }
    }

    @GetMapping(path = "/bySchool/{schoolId}")
    public ResponseEntity<List<Teacher>> getTeachersForSchoolBySchoolId(@PathVariable UUID schoolId,
                                                        @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                        @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize,
                                                        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sort));
        List<Teacher> teachers = teacherService.getTeachersBySchoolId(schoolId, paging);
        return ResponseEntity.ok(teachers);
    }
}
