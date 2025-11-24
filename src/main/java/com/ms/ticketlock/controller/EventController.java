package com.ms.ticketlock.controller;

import com.ms.ticketlock.controller.request.EventRequest;
import com.ms.ticketlock.controller.response.EventResponse;
import com.ms.ticketlock.service.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventController {

    private final EventService service;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Page<EventResponse> getEvents(@RequestParam(name = "size", defaultValue = "15") Integer size,
                                         @RequestParam(name = "page", defaultValue = "0") Integer page,
                                         @RequestParam(name = "orderBy", defaultValue = "DESC") String orderBy) {

        return service.getAllEvents(page, size, orderBy);
    }

    @GetMapping("/{eventId}")
    @ResponseStatus(HttpStatus.OK)
    public EventResponse getEventById(@PathVariable UUID eventId) {
        return service.getEventById(eventId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EventResponse createEvent(@RequestBody @Valid EventRequest request) {
        return service.createEvent(request);
    }
}
