package com.ms.ticketlock.service;

import com.ms.ticketlock.controller.request.ReservationRequest;
import com.ms.ticketlock.controller.response.ReservationResponse;
import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.mapper.ReservationMapper;
import com.ms.ticketlock.message.ReservationExpirationProducer;
import com.ms.ticketlock.repository.ReservationRepository;
import com.ms.ticketlock.repository.TicketRepository;
import com.ms.ticketlock.repository.entity.ReservationEntity;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

import static java.lang.String.format;

@Slf4j
@Service
@RequiredArgsConstructor
public class ReservationService {

    private static final Integer EXPIRATION_MINUTES = 10;

    private final ReservationMapper mapper;
    private final ReservationRepository reservationRepository;
    private final TicketService ticketService;
    private final RedisLockService redisLockService;
    private final ReservationExpirationProducer expirationProducer;

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        var lockKey = format("lock:ticket:%s", request.ticketId());

        if(!redisLockService.acquireLock(lockKey)) {
            throw new RuntimeException("Ticket is being processed");
        }

        try {
            var ticket = ticketService.findById(request.ticketId());
            if(ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new RuntimeException("Ticket is not available");
            }

            ticket.setStatus(TicketStatus.RESERVED);
            var ticketUpdated = ticketService.save(ticket);

            var reservation = mapper.toEntity(request, ticketUpdated, EXPIRATION_MINUTES);
            var saved = reservationRepository.saveAndFlush(reservation);
            expirationProducer.scheduleExpiration(saved.getReservationId());

            return mapper.toResponse(saved);
        } finally {
            redisLockService.releaseLock(lockKey);
        }
    }

    public ReservationResponse getReservation(UUID reservationId) {
        var reservation = getReservationEntity(reservationId);

        return mapper.toResponse(reservation);
    }

    @Transactional
    public ReservationResponse confirmReservation(UUID reservationId) {
        var reservation = getReservationEntity(reservationId);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Reservation is not pending");
        }

        if (LocalDateTime.now().isAfter(reservation.getExpiresAt())) {
            throw new RuntimeException("Reservation has expired");
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.SOLD);
        ticketService.save(ticket);

        return mapper.toResponse(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(UUID reservationId) {
        var reservation = getReservationEntity(reservationId);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new RuntimeException("Reservation is not pending");
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticketService.save(ticket);

        return mapper.toResponse(reservation);
    }

    private ReservationEntity getReservationEntity(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new RuntimeException("Reservation not found"));
    }
}
