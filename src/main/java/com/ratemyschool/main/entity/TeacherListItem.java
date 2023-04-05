package com.ratemyschool.main.entity;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class TeacherListItem {
    private UUID id;
    private String name;
    private float avgStars;
    private int numberOfReview;
    private String schoolName;
}
