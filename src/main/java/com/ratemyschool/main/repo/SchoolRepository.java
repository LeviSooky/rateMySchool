package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.SchoolData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SchoolRepository extends JpaRepository<SchoolData, UUID> {

    @Query("select sd from SchoolData sd where lower(sd.name) = concat('%', :keyword, '%')\n" +
            "and sd.status = com.ratemyschool.main.enums.RMSConstants.ACTIVE")
    Page<SchoolData> findAllBy(String keyword, Pageable pageable);
}
