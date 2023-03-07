package com.ratemyschool.main.model;

import com.ratemyschool.main.dto.Teacher;
import com.ratemyschool.main.enums.EntityStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Table(name = "teacher")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
@EntityListeners(AuditingEntityListener.class)
public class TeacherData implements DomainRepresented<Teacher> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false)
    private String name;
    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "id", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<TeacherReviewData> reviews = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @ToString.Exclude
    private SchoolData school;
    @Column(nullable = false)
    @Enumerated(value = EnumType.STRING)
    private EntityStatus status; //TODO do everywhere and maybe add sex for ui generic logo
    @CreatedDate
    private LocalDateTime creationDate;
    @LastModifiedDate
    private LocalDateTime lastModified;

    private Boolean isMale;

    private float avgRating;

    public void addReview(TeacherReviewData review) {
        review.setTeacher(this);
        reviews.add(review);
    }

    @Override
    public Teacher toDomainModel() {
        return Teacher.builder()
                .id(id)
                .name(name)
                .isMale(isMale)
                .avgRating(avgRating)
                .school(school.toDomainModel())
                .build();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TeacherData that = (TeacherData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode(); //TODO maybe change
    }
}
