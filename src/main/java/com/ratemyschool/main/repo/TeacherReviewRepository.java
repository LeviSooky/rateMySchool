package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.TeacherReviewData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface TeacherReviewRepository extends JpaRepository<TeacherReviewData, UUID> {

    Page<TeacherReviewData> findAllByTeacherId(UUID teacher_id, Pageable pageable);
    List<TeacherReviewData> findAllByStatusFlagOrderByCreationDate(char status);
    List<TeacherReviewData> findAllByStatusFlagIn(List<Character> statuses);
    void deleteAllByLastModifiedBetweenAndStatusFlag(LocalDateTime currentTime, LocalDateTime fromDate, char status);

}
