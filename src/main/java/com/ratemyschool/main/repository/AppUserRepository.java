package com.ratemyschool.main.repository;

import com.ratemyschool.main.entity.UserData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
import java.util.List;
@Repository
public interface AppUserRepository extends JpaRepository<UserData, UUID> {
    Optional<UserData> findByEmail(String email);

    @Query("select ud from UserData ud where lower(ud.email) like concat('%', lower(:keyword), '%')\n " +
            "or lower(ud.firstName) like concat('%', lower(:keyword), '%')\n" +
            "or lower(ud.lastName) like concat('%', lower(:keyword), '%')\n"
    )
    List<UserData> findAllBy(@Param("keyword") String keyword);
}
