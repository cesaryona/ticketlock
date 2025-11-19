package com.ms.ticketlock.controller.response;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

public record EventResponse(
        UUID eventId,
        String name,
        LocalDate date,
        String local,
        Long totalSeats,
        LocalDateTime createdAt) {
}
