package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.RMSReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RMSReviewRepository extends JpaRepository<RMSReview, UUID> {

}
