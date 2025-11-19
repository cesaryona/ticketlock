package com.ms.ticketlock.message;

import com.ms.ticketlock.config.RabbitMQConfig;
import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.repository.ReservationRepository;
import com.ms.ticketlock.repository.TicketRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class ReservationExpirationConsumer {

    private final ReservationRepository reservationRepository;
    private final TicketRepository ticketRepository;

    @RabbitListener(queues = RabbitMQConfig.RESERVATION_EXPIRATION_QUEUE)
    @Transactional
    public void processExpiration(String reservationIdStr) {
        var reservationId = UUID.fromString(reservationIdStr);

        log.info("Processing expiration for reservation: {}", reservationId);

        var reservation = reservationRepository.findById(reservationId)
                .orElse(null);

        if (reservation == null) {
            log.warn("Reservation not found: {}", reservationId);
            return;
        }

        if (reservation.getStatus() != ReservationStatus.PENDING) {
            log.info("Reservation {} already processed, status: {}", reservationId, reservation.getStatus());
            return;
        }

        reservation.setStatus(ReservationStatus.EXPIRED);
        reservationRepository.save(reservation);

        var ticket = reservation.getTicket();
        ticket.setStatus(TicketStatus.AVAILABLE);
        ticketRepository.save(ticket);

        log.info("Reservation {} expired successfully", reservationId);
    }
}