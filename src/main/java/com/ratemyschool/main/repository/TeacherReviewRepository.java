package com.ratemyschool.main.repository;

import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.entity.TeacherReviewData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherReviewRepository extends JpaRepository<TeacherReviewData, UUID> {

    List<TeacherReviewData> findAllByStatusOrderByCreationDate(EntityStatus status);
    List<TeacherReviewData> findAllByStatusIn(List<EntityStatus> statuses);
    void deleteAllByLastModifiedBetweenAndStatus(LocalDateTime currentTime, LocalDateTime fromDate, EntityStatus status);
    @Query("select trd from TeacherReviewData trd left join trd.teacher td where trd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE and td.id = :id")
    Page<TeacherReviewData> findAllActive(@Param("id") UUID id, Pageable pageable);

    @Query("select count(trd) from TeacherReviewData trd left join trd.teacher t where t.id = :teacherId and trd.status = :status")

    Long countAllByTeacherIdAndStatus(UUID teacherId, EntityStatus status);

    Page<TeacherReviewData> findAllByTeacherId(UUID teacherId, Pageable pageable);
}
