package com.ratemyschool.main.model;

import com.ratemyschool.main.dto.School;
import com.ratemyschool.main.enums.EntityStatus;
import lombok.*;
import org.hibernate.Hibernate;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
@Table(name = "school")
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolData implements DomainRepresented<School> {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(name = "website_url")
    private String websiteUrl;
    @Column(name = "creation_date")
    @CreatedDate
    private DateTime creationDate;

    @Enumerated(EnumType.STRING)
    private EntityStatus status;

    @ManyToOne(fetch = FetchType.EAGER)
    private CityData city;

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            fetch = FetchType.LAZY,
            mappedBy = "id")
    @ToString.Exclude
    private List<TeacherData> teachers = new ArrayList<>();

    @OneToMany(
            orphanRemoval = true,
            cascade = CascadeType.ALL,
            mappedBy = "id", fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<SchoolReviewData> reviews = new ArrayList<>();

    @Column(columnDefinition = "float4 default 0")
    private Float avgRating;

    public void addTeacher(TeacherData teacher) {
        teacher.setSchool(this);
        teachers.add(teacher);
    }

    public School toDomainModel() {
        return School.builder()
                .id(this.id)
                .name(this.name)
                .avgRating(avgRating)
                .websiteUrl(this.websiteUrl)
                .status(status)
                .city(city)
                .build();
    }

    public void create(School school) {
        this.name = school.getName();
        this.websiteUrl = school.getWebsiteUrl();
        this.city = school.getCity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        SchoolData that = (SchoolData) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
