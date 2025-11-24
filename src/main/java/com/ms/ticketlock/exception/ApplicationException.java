package com.ms.ticketlock.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends RuntimeException {
    private final ApplicationError error;
    private final transient Object[] args;

    public ApplicationException(ApplicationError error) {
        super(error.getMessage());
        this.error = error;
        this.args = new Object[0];
    }

    public ApplicationException(ApplicationError error, Object... args) {
        super(String.format(error.getMessage(), args));
        this.error = error;
        this.args = args;
    }

    public HttpStatus getStatus() {
        return error.getStatus();
    }
}