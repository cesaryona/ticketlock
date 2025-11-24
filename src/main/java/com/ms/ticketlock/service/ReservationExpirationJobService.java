package com.ms.ticketlock.service;


import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.repository.ReservationRepository;
import com.ms.ticketlock.repository.entity.ReservationEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.javacrumbs.shedlock.spring.annotation.SchedulerLock;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class ReservationExpirationJobService {

    private final ReservationRepository reservationRepository;
    private final ReservationService reservationService;


    @Scheduled(fixedDelay = 30000, initialDelay = 10000)
    @SchedulerLock(name = "expireReservations", lockAtMostFor = "25s", lockAtLeastFor = "5s")
    public void expireReservations() {
        log.debug("Starting reservation expiration job");

        var expiredReservations = reservationRepository
                .findByStatusAndExpiresAtBefore(ReservationStatus.PENDING, LocalDateTime.now());

        if (expiredReservations.isEmpty()) {
            log.debug("No expired reservations found");
            return;
        }

        log.info("Found [{}] expired reservations to process", expiredReservations.size());

        int successCount = 0;
        int errorCount = 0;

        for (ReservationEntity reservation : expiredReservations) {
            try {
                reservationService.expireReservation(reservation.getReservationId());
                successCount++;
            } catch (Exception e) {
                errorCount++;
                log.error("Failed to expire reservation: [{}]", reservation.getReservationId(), e);
            }
        }

        log.info("Finished processing expired reservations - Success: [{}], Errors: [{}]", successCount, errorCount);
    }
}