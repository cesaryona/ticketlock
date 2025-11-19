package com.ms.ticketlock.service;

import com.ms.ticketlock.controller.request.EventRequest;
import com.ms.ticketlock.controller.response.EventResponse;
import com.ms.ticketlock.mapper.EventMapper;
import com.ms.ticketlock.repository.EventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventService {

    private final EventMapper eventMapper;
    private final EventRepository eventRepository;

    public Page<EventResponse> getAllEvents(Integer page, Integer size, String orderBy) {
        Sort sort = Sort.by(Sort.Direction.fromString(orderBy), "createdAt");
        Pageable pageable = PageRequest.of(page, size, sort);

        return eventRepository.findAll(pageable)
                .map(eventMapper::toResponse);
    }

    public EventResponse getEventById(UUID eventId) {
        var eventEntity = eventRepository.findById(eventId).orElseThrow(() -> new RuntimeException("not found"));

        return eventMapper.toResponse(eventEntity);
    }

    public EventResponse createEvent(EventRequest request) {
        var eventEntity = eventRepository.save(eventMapper.toEntity(request));

        return eventMapper.toResponse(eventEntity);
    }
}
