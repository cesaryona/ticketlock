package com.ms.ticketlock.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Builder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record EventResponse(
        UUID eventId,
        String name,
        LocalDate date,
        String local,
        Long totalSeats,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt) {
}
