package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.SchoolReviewData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolReviewRepository extends JpaRepository<SchoolReviewData, UUID> {

    @Query("select srd from SchoolReviewData srd where srd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<SchoolReviewData> findAllActive(Pageable pageable);
}
