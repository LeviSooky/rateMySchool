package com.ratemyschool.main.model;

import com.ratemyschool.main.dto.School;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.joda.time.DateTime;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class SchoolData {
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

    private char status;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<TeacherData> teachers;

    public void addTeacher(TeacherData teacher) {
        teacher.setSchoolData(this);
        teachers.add(teacher);
    }

    public School toDomainModel() {
        return School.builder()
                .id(this.id)
                .name(this.name)
                .websiteUrl(this.websiteUrl)
                .build();
    }

    public void create(School school) {
        this.name = school.getName();
        this.websiteUrl = school.getWebsiteUrl();
    }
}
