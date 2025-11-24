package com.ms.ticketlock.controller;

import com.ms.ticketlock.controller.request.TicketBatchRequest;
import com.ms.ticketlock.controller.request.TicketRequest;
import com.ms.ticketlock.controller.response.TicketResponse;
import com.ms.ticketlock.service.TicketService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/events/{eventId}/tickets")
@RequiredArgsConstructor
public class TicketController {

    private final TicketService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<TicketResponse> getTickets(@PathVariable UUID eventId,
                                           @RequestParam(name = "size", defaultValue = "15") Integer size,
                                           @RequestParam(name = "page", defaultValue = "0") Integer page,
                                           @RequestParam(name = "orderBy", defaultValue = "DESC") String orderBy) {
        return service.getTicketsByEvent(eventId, page, size, orderBy);
    }

    @GetMapping("/available")
    @ResponseStatus(HttpStatus.OK)
    public Page<TicketResponse> getAvailableTickets(@PathVariable UUID eventId,
                                                    @RequestParam(name = "size", defaultValue = "15") Integer size,
                                                    @RequestParam(name = "page", defaultValue = "0") Integer page,
                                                    @RequestParam(name = "orderBy", defaultValue = "DESC") String orderBy) {
        return service.getAvailableTicketsByEvent(eventId, page, size, orderBy);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public List<TicketResponse> createTickets(@PathVariable UUID eventId,
                                              @Valid @RequestBody TicketBatchRequest request) {
        return service.createTickets(eventId, request);
    }

}