package com.ratemyschool.main.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class RMSSchool {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @OneToMany
    private List<RMSTeacher> teachers;

}
