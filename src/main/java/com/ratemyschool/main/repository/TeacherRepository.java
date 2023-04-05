package com.ratemyschool.main.repository;

import com.ratemyschool.main.entity.TeacherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TeacherRepository extends JpaRepository<TeacherData, UUID> {
    Page<TeacherData> findAllBySchoolId(UUID schoolId, Pageable pageable);

    Optional<TeacherData> findByNameLikeAndSchoolNameLike(String name, String schoolName);

    @Query("select td from TeacherData td left join td.school s left join s.city c where td.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE\n" +
            "and (lower(td.name) like lower(concat('%', :keyword, '%'))\n" +
            "or lower(s.name) like lower(concat('%', :keyword, '%')))\n"
    )
    Page<TeacherData> findAllActiveBy(@Param("keyword") String keyword, Pageable pageable);

    @Query("select td from TeacherData td where lower(td.name) like lower(concat('%', :keyword, '%'))\n" +
            "or lower(td.school.name) like lower(concat('%', :keyword, '%'))\n"
    )
    Page<TeacherData> findAllBy(@Param("keyword") String keyword, Pageable pageable);

    @Query("select td from TeacherData td where td.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<TeacherData> findAllActive(Pageable pageable);
}

