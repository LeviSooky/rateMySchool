package com.ratemyschool.main.dto;

import com.ratemyschool.main.enums.EntityStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
@AllArgsConstructor
@Data
public class TeacherReview {
    private UUID id;
    private String content;
    private LocalDateTime creationDate;
    private Integer stars;
    private EntityStatus status;
}
