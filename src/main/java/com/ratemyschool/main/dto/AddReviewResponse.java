package com.ratemyschool.main.dto;

import com.ratemyschool.main.enums.EntityStatus;
import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;
import java.util.UUID;

@Builder
@Getter
public class AddReviewResponse implements Serializable {

    private UUID id;
    private Integer stars;
    private EntityStatus status;
}