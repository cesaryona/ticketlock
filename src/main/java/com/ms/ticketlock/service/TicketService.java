package com.ms.ticketlock.service;

import com.ms.ticketlock.controller.request.TicketBatchRequest;
import com.ms.ticketlock.controller.response.TicketResponse;
import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.mapper.TicketMapper;
import com.ms.ticketlock.repository.EventRepository;
import com.ms.ticketlock.repository.TicketRepository;
import com.ms.ticketlock.repository.entity.TicketEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TicketService {

    private final TicketMapper mapper;
    private final TicketRepository repository;
    private final EventRepository eventRepository;

    public TicketEntity findById(UUID id) {
        return repository.findById(id).orElseThrow(() -> new RuntimeException("ticket not found"));
    }

    public Page<TicketResponse> getTicketsByEvent(UUID eventId, Integer page, Integer size, String orderBy) {
        Pageable pageable = getPageable(page, size, orderBy);

        return repository.findByEvent_EventId(eventId, pageable)
                .map(mapper::toResponse);
    }

    public Page<TicketResponse> getAvailableTicketsByEvent(UUID eventId, Integer page, Integer size, String orderBy) {
        Pageable pageable = getPageable(page, size, orderBy);

        return repository.findByEvent_EventIdAndStatus(eventId, TicketStatus.AVAILABLE, pageable)
                .map(mapper::toResponse);
    }

    public List<TicketResponse> createTickets(UUID eventId, TicketBatchRequest request) {
        var event = eventRepository.findById(eventId)
                .orElseThrow(() -> new RuntimeException("Event not found: " + eventId));

        var tickets = request.tickets().stream()
                .map(ticketRequest -> mapper.toEntity(ticketRequest, event))
                .toList();

        return repository.saveAll(tickets).stream()
                .map(mapper::toResponse)
                .toList();
    }

    public TicketEntity save(TicketEntity ticketEntity) {
        return repository.save(ticketEntity);
    }

    private Pageable getPageable(Integer page, Integer size, String orderBy) {
        return PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderBy), "createdAt"));
    }
}