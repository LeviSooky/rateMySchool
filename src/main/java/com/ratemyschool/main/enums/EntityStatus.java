package com.ratemyschool.main.enums;

import lombok.Getter;

@Getter
public enum EntityStatus {
    ACTIVE,
    DELETED,
    PENDING,
    NOT_ACCEPTABLE,
    SENTIMENT_FAILED,
    TRANSLATION_FAILED;
}
