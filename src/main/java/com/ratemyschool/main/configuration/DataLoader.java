package com.ratemyschool.main.configuration;

import com.ratemyschool.main.model.Teacher;
import com.ratemyschool.main.model.User;
import com.ratemyschool.main.service.TeacherService;
import com.ratemyschool.main.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.UUID;

@RequiredArgsConstructor
@Component
public class DataLoader implements CommandLineRunner {

    private final TeacherService teacherService;
    private final UserService userService;

    @Override
    public void run(String... args) throws Exception {
        Teacher teacher = new Teacher();
        teacher.setName("levi");
        teacherService.addTeacher(teacher);
        User user = new User();
        user.setId(UUID.randomUUID());
        user.setAdmin(true);
        user.setEmail("levike@kukac.com");
        user.setPassword("fing");
        userService.addUser(user);
    }
}
