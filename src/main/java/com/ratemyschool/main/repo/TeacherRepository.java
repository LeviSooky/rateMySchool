package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, UUID> {
    Page<Teacher> findAllByStatusEquals(char status, Pageable pageable);
    Page<Teacher> findAllBySchoolId(UUID schoolId, Pageable pageable);
    Optional<Teacher> findByNameLikeAndSchoolNameLike(String name, String schoolName);
   
}
