package com.ratemyschool.main.service;

import com.ratemyschool.main.model.UserData;
import com.ratemyschool.main.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;

    public void addUser(UserData user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }
}
