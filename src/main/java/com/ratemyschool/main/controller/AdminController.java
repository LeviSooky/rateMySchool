package com.ratemyschool.main.controller;


import com.ratemyschool.main.dto.User;
import com.ratemyschool.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@Validated
@RequestMapping("admin")
public class AdminController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteBy(id);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/user")
    public ResponseEntity<Void> update(@Valid @RequestBody User user) {
        userService.update(user);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<List<User>> findAll(@RequestParam(required = false) String keyword) {
        List<User> all = userService.findAll(keyword);
        return ResponseEntity.ok(all);
    }
}
