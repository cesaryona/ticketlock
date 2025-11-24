package com.ms.ticketlock.exception;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<SimpleError> handleApplicationException(ApplicationException ex,
                                                                  HttpServletRequest request) {
        var error = SimpleError.builder()
                .code(ex.getStatus().value())
                .message(ex.getMessage())
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(ex.getStatus())
                .body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> handleValidationException(MethodArgumentNotValidException ex,
                                                                     HttpServletRequest request) {
        var errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> ValidationError.FieldError.builder()
                        .field(error.getField())
                        .message(error.getDefaultMessage())
                        .build())
                .toList();

        var error = ValidationError.builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message("Validation error")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .errors(errors)
                .build();

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(error);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<SimpleError> handleGenericException(Exception ex,
                                                              HttpServletRequest request) {
        log.error("Unexpected exception on path: [{}]", request.getRequestURI(), ex);

        var error = SimpleError.builder()
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                .message("Internal server error")
                .timestamp(LocalDateTime.now())
                .path(request.getRequestURI())
                .build();

        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(error);
    }
}