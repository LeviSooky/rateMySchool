package com.ratemyschool.main.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

import java.util.UUID;

@Builder
public class Teacher {

    private UUID id;
    private String name;
    private School school;
}
