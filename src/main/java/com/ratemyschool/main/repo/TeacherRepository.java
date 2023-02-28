package com.ratemyschool.main.repo;

import com.ratemyschool.main.model.TeacherData;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;
@Repository
public interface TeacherRepository extends JpaRepository<TeacherData, UUID> {
    Page<TeacherData> findAllByStatusEquals(char status, Pageable pageable);
    @EntityGraph(attributePaths = "school")
    Page<TeacherData> findAllBySchoolId(UUID schoolId, Pageable pageable);
    Optional<TeacherData> findByNameLikeAndSchoolNameLike(String name, String schoolName);
    boolean existsByIdAndStatus(UUID teacherId, char status);
//    @Query("SELECT t.id, t.name, AVG(r.stars), COUNT(r.id), s.name\n" +
//            "FROM teacher t\n" +
//            "JOIN review r ON t.id = r.teacher_id\n" +
//            "JOIN school s ON t.school_id = s.id\n" +
//            "WHERE t.id = :teacherId "+
//            "AND t.status = 'A'\n" +
//            "AND  r.status_flag = 'A'\n" +
//            "GROUP BY t.id\n" +
//            "LIMIT(:#{#pageable.pageSize})\n" +
//            "ORDER BY :#{#pageable.sort} :sortDirection")
//    List<TeacherListItem> getThem(@Param("teacherId") UUID teacherId, @Param("pageable") Pageable pageable);
    //TODO
    @Query("select td from TeacherData td where td.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE and lower(td.name) like lower(concat('%', :keyword, '%'))")
    Page<TeacherData> findAllActiveBy(@Param("keyword") String keyword, Pageable pageable);

    @Query("select td from TeacherData td where td.status = com.ratemyschool.main.enums.EntityStatus.ACTIVE")
    Page<TeacherData> findAllActive(Pageable pageable);
}

