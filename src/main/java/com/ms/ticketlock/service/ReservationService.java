package com.ms.ticketlock.service;

import com.ms.ticketlock.controller.request.ReservationRequest;
import com.ms.ticketlock.controller.response.ReservationResponse;
import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.exception.ApplicationError;
import com.ms.ticketlock.exception.ApplicationException;
import com.ms.ticketlock.mapper.ReservationMapper;
import com.ms.ticketlock.repository.ReservationRepository;
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

    @Transactional
    public ReservationResponse createReservation(ReservationRequest request) {
        var lockKey = format("lock:ticket:[%s]", request.ticketId());

        if (!redisLockService.acquireLock(lockKey)) {
            throw new ApplicationException(ApplicationError.TICKET_BEING_PROCESSED, request.ticketId());
        }

        try {
            var ticket = ticketService.findById(request.ticketId());
            if (ticket.getStatus() != TicketStatus.AVAILABLE) {
                throw new ApplicationException(ApplicationError.TICKET_NOT_AVAILABLE, request.ticketId());
            }

            ticket.setStatus(TicketStatus.RESERVED);
            var ticketUpdated = ticketService.save(ticket);

            var reservation = mapper.toEntity(request, ticketUpdated, EXPIRATION_MINUTES);
            var saved = reservationRepository.saveAndFlush(reservation);

            log.info("Reservation created: [{}], expires at: [{}]", saved.getReservationId(), saved.getExpiresAt());

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
            throw new ApplicationException(ApplicationError.RESERVATION_NOT_PENDING, reservationId);
        }

        if (LocalDateTime.now().isAfter(reservation.getExpiresAt())) {
            throw new ApplicationException(ApplicationError.RESERVATION_EXPIRED, reservationId);
        }

        reservation.setStatus(ReservationStatus.CONFIRMED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.SOLD);
        ticketService.save(ticket);

        log.info("Reservation [{}] confirmed successfully", reservationId);

        return mapper.toResponse(reservation);
    }

    @Transactional
    public ReservationResponse cancelReservation(UUID reservationId) {
        var reservation = getReservationEntity(reservationId);

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            throw new ApplicationException(ApplicationError.RESERVATION_NOT_PENDING, reservationId);
        }

        reservation.setStatus(ReservationStatus.CANCELLED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticketService.save(ticket);

        log.info("Reservation [{}] cancelled successfully", reservationId);

        return mapper.toResponse(reservation);
    }

    @Transactional
    public void expireReservation(UUID reservationId) {
        var reservation = reservationRepository.findById(reservationId)
                .orElse(null);

        if (reservation == null) {
            log.debug("Reservation [{}] not found, skipping expiration", reservationId);
            return;
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            log.debug("Reservation [{}] already in status [{}], skipping expiration",
                    reservationId, reservation.getStatus());
            return;
        }

        if (LocalDateTime.now().isBefore(reservation.getExpiresAt())) {
            log.warn("Reservation [{}] has not expired yet (expires at: [{}]), skipping",
                    reservationId, reservation.getExpiresAt());
            return;
        }

        reservation.setStatus(ReservationStatus.EXPIRED);
        reservation.setUpdatedAt(LocalDateTime.now());
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticketService.save(ticket);

        log.info("Reservation [{}] expired successfully", reservationId);
    }

    private ReservationEntity getReservationEntity(UUID reservationId) {
        return reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ApplicationException(ApplicationError.RESERVATION_NOT_FOUND, reservationId));
    }
}