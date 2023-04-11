package com.ratemyschool.main.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private final String path;
    private final String message;
    private final Integer statusCode;
    private final LocalDateTime time;
}
