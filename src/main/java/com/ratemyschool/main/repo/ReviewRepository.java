package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.ReviewData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<ReviewData, UUID> {

    Page<ReviewData> findAllByTeacherId(UUID teacher_id, Pageable pageable);
    List<ReviewData> findAllByStatusFlagOrderByCreationDate(char status);
    List<ReviewData> findAllByStatusFlagIn(List<Character> statuses);
    void deleteAllByLastModifiedBetweenAndStatusFlag(LocalDateTime currentTime, LocalDateTime fromDate, char status);

}
