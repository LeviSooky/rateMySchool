package com.ratemyschool.main.service;

import com.ratemyschool.main.exception.RmsRuntimeException;
import com.ratemyschool.main.model.User;
import com.ratemyschool.main.model.UserData;
import com.ratemyschool.main.repo.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

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
        userData.setLastName(user.getLastName());
        userData.setFirstName(user.getFirstName());
        userData.setAdmin(user.isAdmin());
        userRepository.save(userData);
    }

    public void deleteBy(UUID id) {
        userRepository.findById(id).orElseThrow(() -> new RmsRuntimeException("user not found"));
        userRepository.deleteById(id);
    }

    public List<User> findAll(String keyword) {
        if (Strings.isNotBlank(keyword)) {
            return userRepository.findAllBy(keyword).stream().map(UserData::toDomainModel).collect(Collectors.toList());
        } else {
            return userRepository.findAll().stream().map(UserData::toDomainModel).collect(Collectors.toList());
        }
    }

    public void update(User user) {
        UserData userData = userRepository.findById(user.getId())
                .orElseThrow(() -> new RmsRuntimeException("user not found"));
        if (Strings.isNotBlank(user.getPassword())) {
            userData.setPassword(passwordEncoder.encode(user.getPassword()));
        }
        userData.setEmail(user.getEmail());
        userData.setLastName(user.getLastName());
        userData.setFirstName(user.getFirstName());
        userData.setAdmin(user.isAdmin());
        userRepository.save(userData);
    }
}
