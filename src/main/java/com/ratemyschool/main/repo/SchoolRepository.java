package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.SchoolData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SchoolRepository extends JpaRepository<SchoolData, UUID> {

    @Query("select sd from SchoolData sd where lower(sd.name) like lower(concat('%', :keyword, '%'))\n" +
            "and sd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<SchoolData> findAllActiveBy(@Param("keyword") String keyword, Pageable pageable);

    @Query("select sd from SchoolData sd where sd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<SchoolData> findAllActive(Pageable pageable);

    @Query("select sd from SchoolData sd where lower(sd.name) like lower(concat('%', :keyword, '%'))\n")
    Page<SchoolData> findAllBy(@Param("keyword") String keyword, Pageable pageable);
}
