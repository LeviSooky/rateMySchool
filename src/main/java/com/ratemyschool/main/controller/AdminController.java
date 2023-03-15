package com.ratemyschool.main.controller;


import com.ratemyschool.main.model.User;
import com.ratemyschool.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("admin")
public class AdminController {

    private final UserService userService;

    @PostMapping("/user")
    public ResponseEntity<Void> createUser(@RequestBody User user) {
        userService.addUser(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/user/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        userService.deleteBy(id);
        return ResponseEntity.ok().build();
    }
}
