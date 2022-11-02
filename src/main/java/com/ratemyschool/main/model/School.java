package com.ratemyschool.main.model;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.joda.time.DateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@Entity
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class School {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @Column(name = "website_url")
    private String websiteUrl;
    @Column(name = "creation_date")
    private DateTime creationDate;
    private char status;
    @OneToMany(orphanRemoval = true, cascade = CascadeType.ALL)
    private List<Teacher> teachers;

    public void addTeacher(Teacher teacher) {
        teacher.setSchool(this);
        teachers.add(teacher);
    }
}
