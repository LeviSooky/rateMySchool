package com.ratemyschool.main.entity;

import com.ratemyschool.main.dto.DomainRepresented;
import com.ratemyschool.main.dto.SchoolReview;
import com.ratemyschool.main.enums.EntityStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "school_review")
@Builder(toBuilder = true)
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class SchoolReviewData implements DomainRepresented<SchoolReview> {

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
    private Float sentimentScore;
    private UUID deleteKey;
    private Integer stars;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EntityStatus status;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastModified;
    @ManyToOne(fetch = FetchType.LAZY)
    @ToString.Exclude
    private SchoolData school;


    @Override
    public SchoolReview toDomainModel() {
        return SchoolReview.builder()
                .id(id)
                .content(content)
                .stars(stars)
                .creationDate(creationDate)
                .status(status)
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolReviewData that = (SchoolReviewData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
