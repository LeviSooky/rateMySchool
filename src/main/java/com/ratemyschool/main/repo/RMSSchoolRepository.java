package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.RMSSchool;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface RMSSchoolRepository extends JpaRepository<RMSSchool, UUID> {
}
