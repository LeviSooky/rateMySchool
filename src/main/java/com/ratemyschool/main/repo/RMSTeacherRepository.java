package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.RMSTeacher;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.UUID;
@Repository
public interface RMSTeacherRepository extends JpaRepository<RMSTeacher, UUID> {
    Page<RMSTeacher> findAllByStatusEquals(char status, Pageable pageable);
}
