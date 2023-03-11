package com.ratemyschool.main.service;

import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.User;
import com.ratemyschool.main.model.UserData;
import com.ratemyschool.main.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {
    private final PasswordEncoder passwordEncoder;
    private final AppUserRepository userRepository;

    public void addUser(User user) {
        if (user.getId() != null) {
            throw new IllegalStateException("new user should not have id");
        }
        UserData userData = new UserData();
        userData.setAdmin(user.isAdmin());
        userData.setPassword(passwordEncoder.encode(user.getPassword()));
        userData.setEmail(user.getEmail());
        userRepository.save(userData);
    }

    public void deleteBy(UUID id) {
        userRepository.findById(id).orElseThrow(() -> new RmsRuntimeException("user not found"));
        userRepository.deleteById(id);
    }
}
