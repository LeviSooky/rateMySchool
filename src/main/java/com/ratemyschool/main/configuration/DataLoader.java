package com.ratemyschool.main.configuration;

import com.ratemyschool.main.service.TeacherService;
import com.ratemyschool.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final TeacherService teacherService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
    }
}
