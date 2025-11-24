package com.ms.ticketlock.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ApplicationError {
    EVENT_NOT_FOUND(HttpStatus.NOT_FOUND, "Event not found, id: [%s]"),

    TICKET_NOT_FOUND(HttpStatus.NOT_FOUND, "Ticket [%s] not found"),
    TICKET_NOT_AVAILABLE(HttpStatus.BAD_REQUEST, "Ticket [%s] is not available"),
    TICKET_BEING_PROCESSED(HttpStatus.CONFLICT, "Ticket [%s] is being processed"),

    RESERVATION_NOT_FOUND(HttpStatus.NOT_FOUND, "Reservation [%s] not found"),
    RESERVATION_NOT_PENDING(HttpStatus.BAD_REQUEST, "Reservation [%s] is not pending"),
    RESERVATION_EXPIRED(HttpStatus.BAD_REQUEST, "Reservation [%s] has expired");

    private final HttpStatus status;
    private final String message;

    ApplicationError(HttpStatus status, String message) {
        this.status = status;
        this.message = message;
    }
}