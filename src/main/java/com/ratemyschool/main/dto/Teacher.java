package com.ratemyschool.main.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Teacher {

    private UUID id;
    private String name;
    private School school;
    private Boolean isMale;
    private float avgRating;
}
