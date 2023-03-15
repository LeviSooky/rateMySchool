package com.ratemyschool.main.model;

import com.ratemyschool.main.dto.TeacherReview;
import com.ratemyschool.main.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Entity
@Table(name = "teacher_review")
@Builder(toBuilder = true)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
public class TeacherReviewData implements DomainRepresented<TeacherReview> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(length = 2000)
    private String contentInEnglish;
    private float sentimentScore;
    private UUID deleteKey;
    private Integer stars;
    @Enumerated(EnumType.STRING)
    private EntityStatus status;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @ManyToOne
    private TeacherData teacher;

    @Override
    public TeacherReview toDomainModel() {
        return TeacherReview.builder()
                .id(id)
                .content(content)
                .creationDate(creationDate)
                .stars(stars)
                .build();
    }
}
