package com.ratemyschool.main.entity;

import java.time.LocalDateTime;


public class RMSResponse <T> {
    private final String message;
    private final LocalDateTime time;
    private final T data;

    public RMSResponse(String message, T data) {
        this.message = message;
        this.time = LocalDateTime.now();
        this.data = data;
    }
}
