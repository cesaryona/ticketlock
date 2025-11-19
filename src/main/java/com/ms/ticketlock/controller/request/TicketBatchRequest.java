package com.ms.ticketlock.controller.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record TicketBatchRequest(
        @NotNull(message = "Tickets list is required")
        @Size(min = 1, message = "At least one ticket is required")
        List<TicketRequest> tickets) {
}
