package com.ratemyschool.main.controller;

import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.TeacherData;
import com.ratemyschool.main.service.SchoolService;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
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
@Log4j2
@RequestMapping("/api/teachers")
public class TeacherController {

    private final TeacherService teacherService;
    private final SchoolService schoolService;
//    @GetMapping(path = "/all")
//    public ResponseEntity<List<TeacherData>> getTeachers(
//        @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
//        @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize,
//        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
//        @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {
//
//        Sort.Direction direction = Sort.Direction.ASC.name().equals(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
//        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(direction, sort));
//        List<TeacherData> teacherList = teacherService.getTeachers(paging);
//        return ResponseEntity.ok(teacherList);
//    }

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

    //TODO to admin
    @DeleteMapping(path = "/delete/{teacherId}")
    public ResponseEntity<Boolean> deleteTeacher(@PathVariable UUID teacherId) {
        try {
            TeacherData teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacher);
            return ResponseEntity.ok(Boolean.TRUE);
        }
         catch (RmsRuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Teacher Not Found");
        }
    }

    @PutMapping
    public ResponseEntity<Teacher> edit(@RequestBody Teacher teacher, @RequestParam UUID schoolId) {
        return ResponseEntity.ok(teacherService.edit(teacher, schoolId));
    }

    @GetMapping(path = "/bySchool/{schoolId}")
    public ResponseEntity<List<TeacherData>> getTeachersForSchoolBySchoolId(@PathVariable UUID schoolId,
                                                                            @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
                                                                            @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize,
                                                                            @RequestParam(name = "sort", required = false, defaultValue = "name") String sort,
                                                                            @RequestParam(name = "sortDirection", required = false, defaultValue = "ASC") String sortDirection) {
        Sort.Direction direction = Sort.Direction.ASC.name().equalsIgnoreCase(sortDirection) ? Sort.Direction.ASC : Sort.Direction.DESC;
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(direction, sort));
        List<TeacherData> teachers = teacherService.getTeachersBySchoolId(schoolId, paging);
        return ResponseEntity.ok(teachers);
    }

//    @PostMapping("/add/{schoolId}")
//    public ResponseEntity<String> addTeacher(@PathVariable UUID schoolId, @RequestBody TeacherData teacher) {
//
//        try {
//            schoolService.addTeacherToSchool(schoolId, teacher);
//        } catch (RmsRuntimeException e) {
//            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "school does not exist");
//        }
//        return ResponseEntity.ok("success");
//    }

    @PostMapping("/add/{schoolId}")
    public ResponseEntity<Teacher> create(@RequestBody Teacher teacher, @PathVariable UUID schoolId) {
        log.info("REST request to create teacher: {}", teacher);
        Teacher saved = teacherService.save(teacher, schoolId);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

}
