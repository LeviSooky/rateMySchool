package com.ratemyschool.main.dto;

import com.ratemyschool.main.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Builder
@Data
@AllArgsConstructor
public class Teacher {

    private UUID id;
    @NotBlank
    @Size(min = 8)
    private String name;
    private School school;
    @NotNull
    private Boolean isMale;
    private Float avgRating;
    private EntityStatus status;
}
