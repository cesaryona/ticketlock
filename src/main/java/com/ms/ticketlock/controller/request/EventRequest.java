package com.ms.ticketlock.controller.request;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.time.LocalDate;

public record EventRequest(
        @NotBlank(message = "Name is required")
        String name,

        @NotNull(message = "Date is required")
        @Future(message = "Date must be in the future")
        LocalDate date,

        @NotBlank(message = "Local is required")
        String local,

        @NotNull(message = "Total seats is required")
        @Positive(message = "Total seats must be positive")
        Integer totalSeats) {
}