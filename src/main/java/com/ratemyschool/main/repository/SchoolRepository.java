package com.ratemyschool.main.repository;

import com.ratemyschool.main.entity.SchoolData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.UUID;
@Repository
public interface SchoolRepository extends JpaRepository<SchoolData, UUID> {

    @EntityGraph(attributePaths = {"city"})
    @Query("select sd from SchoolData sd where sd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE\n " +
            "and (lower(sd.name) like lower(concat('%', :keyword, '%'))\n" +
            "or (sd.city.name is not null and lower(sd.city.name) like lower(concat('%', :keyword, '%')))\n" +
            "or (sd.websiteUrl is not null and lower(sd.websiteUrl) like lower(concat('%', :keyword, '%'))))\n"
            )
    Page<SchoolData> findAllActiveBy(@Param("keyword") String keyword, Pageable pageable);

    @EntityGraph(attributePaths = {"city"})
    @Query("select sd from SchoolData sd where sd.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<SchoolData> findAllActive(Pageable pageable);

    @EntityGraph(attributePaths = {"city"})
    @Query(
            "select sd from SchoolData sd where lower(sd.name) like lower(concat('%', :keyword, '%'))\n" +
            "or (sd.city.name is not null and lower(sd.city.name) like lower(concat('%', :keyword, '%')))\n" +
            "or (sd.websiteUrl is not null and lower(sd.websiteUrl) like lower(concat('%', :keyword, '%')))\n"
    )
    Page<SchoolData> findAllBy(@Param("keyword") String keyword, Pageable pageable);
}
