package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface UserRepository extends JpaRepository<UserData, UUID> {
    Optional<UserData> findByEmail(String email);
}
