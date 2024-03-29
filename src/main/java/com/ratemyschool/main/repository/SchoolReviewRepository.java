package com.ratemyschool.main.repository;

import com.ratemyschool.main.enums.EntityStatus;
import com.ratemyschool.main.entity.SchoolReviewData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SchoolReviewRepository extends JpaRepository<SchoolReviewData, UUID> {

    @Query("select srd from SchoolReviewData srd left join srd.school s where srd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE\n" +
            "and s.id = :id\n"
    )
    Page<SchoolReviewData> findAllActiveBy(@Param("id") UUID id, Pageable pageable);

    @Query("select srd from SchoolReviewData srd left join srd.school s where s.id = :id\n"
    )
    Page<SchoolReviewData> findAllBy(@Param("id") UUID id, Pageable pageable);

    @Query("select count(sr) from SchoolReviewData sr left join sr.school s where s.id = :schoolId and sr.status = :status")
    Long countAllBySchoolIdAndStatus(@Param("schoolId") UUID schoolId,
                                     @Param("status") EntityStatus status);
}
