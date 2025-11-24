package com.ms.ticketlock.controller;

import com.ms.ticketlock.controller.request.ReservationRequest;
import com.ms.ticketlock.controller.response.ReservationResponse;
import com.ms.ticketlock.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService service;

    @PostMapping("/tickets")
    @ResponseStatus(HttpStatus.CREATED)
    public ReservationResponse createReservation(@RequestBody @Valid ReservationRequest request) {
        return service.createReservation(request);
    }

    @GetMapping("/{reservationId}")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse getReservation(@PathVariable UUID reservationId) {
        return service.getReservation(reservationId);
    }

    @PostMapping("/{reservationId}/confirm")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse confirmReservation(@PathVariable UUID reservationId) {
        return service.confirmReservation(reservationId);
    }

    @PostMapping("/{reservationId}/cancel")
    @ResponseStatus(HttpStatus.OK)
    public ReservationResponse cancelReservation(@PathVariable UUID reservationId) {
        return service.cancelReservation(reservationId);
    }
}