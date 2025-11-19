package com.ms.ticketlock.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ms.ticketlock.enums.TicketStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record TicketResponse(
        UUID ticketId,
        UUID eventId,
        String seatRow,
        String seatNumber,
        TicketStatus status,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt) {
}