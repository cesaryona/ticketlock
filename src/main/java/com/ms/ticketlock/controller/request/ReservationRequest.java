package com.ms.ticketlock.controller.request;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ReservationRequest(
        @NotNull(message = "Ticket ID is required")
        UUID ticketId,
        @NotNull(message = "User ID is required")
        UUID userId) {
}