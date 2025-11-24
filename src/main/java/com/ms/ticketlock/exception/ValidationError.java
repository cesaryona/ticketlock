package com.ms.ticketlock.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor
public class ValidationError {

    private Integer code;
    private String message;
    private LocalDateTime timestamp;
    private String path;
    private List<FieldError> errors;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}