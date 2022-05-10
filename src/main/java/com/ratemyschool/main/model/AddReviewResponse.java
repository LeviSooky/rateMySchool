package com.ratemyschool.main.model;

import lombok.Builder;
import lombok.Getter;

import java.io.Serializable;

@Builder
@Getter
public class AddReviewResponse implements Serializable {
    private byte stars;
    private char status;
}