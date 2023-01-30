package com.ratemyschool.main.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class TeacherData {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false)
    private String name;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<ReviewData> reviews = new ArrayList<>();
    @ManyToOne
    private SchoolData schoolData;
    private char status;
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastModified;

    public void addReview(ReviewData review) {
        review.setTeacher(this);
        reviews.add(review);
    }
}
