package com.ratemyschool.main;

import com.ratemyschool.main.enums.RMSConstants;
import com.ratemyschool.main.model.RMSResponse;
import com.ratemyschool.main.model.RMSTeacher;
import com.ratemyschool.main.service.TeacherService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.connector.Response;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/teacher")
public class TeacherController {

    private final TeacherService teacherService;
    @GetMapping(path = "/all")
    public ResponseEntity<List<RMSTeacher>> getTeachers(
        @RequestParam(name = "pageNo", required = false, defaultValue = "0") Integer pageNo,
        @RequestParam(name = "pageSize", required = false, defaultValue = "30") Integer pageSize,
        @RequestParam(name = "sort", required = false, defaultValue = "name") String sort) {

        List<RMSTeacher> teacherList = teacherService.getTeachers(pageNo, pageSize, sort);
        return ResponseEntity.ok(teacherList);
    }

    @PutMapping(path = "/add")
    public void addTeacher(@RequestBody RMSTeacher teacher) {
        teacherService.addTeacher(teacher);
    }

    @DeleteMapping(path = "/delete/{teacherId}")
    public ResponseEntity<Boolean> deleteTeacher(@PathVariable UUID teacherId) {
        try {
            RMSTeacher teacher = teacherService.getTeacherById(teacherId);
            teacherService.deleteTeacher(teacher);
            return ResponseEntity.ok(Boolean.TRUE);
        }
         catch (RuntimeException e) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Teacher Not Found", e);
        }
    }
}
