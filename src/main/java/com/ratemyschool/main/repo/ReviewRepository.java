package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReviewRepository extends JpaRepository<Review, UUID> {

    Page<Review> findAllByTeacherId(UUID teacher_id, Pageable pageable);
    List<Review> findAllByStatusFlagOrderByCreationDate(char status);
    List<Review> findAllByStatusFlagIn(List<Character> statuses);

}
