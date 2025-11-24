package com.ms.ticketlock.exception;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<SimplesError> handleApplicationException(ApplicationException ex,
                                                                   HttpServletRequest request) {
        log.warn("Application exception: {} - {}", ex.getStatus(), ex.getMessage());

        var error = SimplesError.builder()
                .code(ex.getStatus().value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(ex.getStatus()).body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimplesError> handleGenericException(Exception ex,
                                                               HttpServletRequest request) {
        log.error("Unexpected exception", ex);

        var error = SimplesError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(error);
    }
}