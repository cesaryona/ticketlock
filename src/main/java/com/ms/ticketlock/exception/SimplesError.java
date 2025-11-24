package com.ms.ticketlock.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
public class SimplesError {

    private Integer code;
    private String message;
    private LocalDateTime timestamp;
    private String path;

}