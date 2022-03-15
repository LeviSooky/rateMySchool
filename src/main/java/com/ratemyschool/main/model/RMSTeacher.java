package com.ratemyschool.main.model;

import lombok.Data;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@Entity
public class RMSTeacher {
    @Id
    private UUID id;
    @Column(nullable = false)
    private String name;
    @OneToMany(orphanRemoval = true)
    private List<RMSReview> reviews = new ArrayList<>();
    @ManyToOne
    private RMSSchool school;
    private char status;
}
