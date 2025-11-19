package com.ms.ticketlock.controller.request;

import jakarta.validation.constraints.NotBlank;

public record TicketRequest(
        @NotBlank(message = "Seat row is required")
        String seatRow,

        @NotBlank(message = "Seat number is required")
        String seatNumber) {
}