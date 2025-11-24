package com.ms.ticketlock.controller.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ms.ticketlock.enums.ReservationStatus;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record ReservationResponse(
        UUID reservationId,
        UUID ticketId,
        UUID userId,
        ReservationStatus status,
        LocalDateTime expiresAt,

        @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
        LocalDateTime createdAt) {
}