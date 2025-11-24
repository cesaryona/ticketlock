package com.ms.ticketlock.repository;


import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.repository.entity.ReservationEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface ReservationRepository extends JpaRepository<ReservationEntity, UUID> {

    @Query("SELECT r FROM ReservationEntity r " +
            "WHERE r.status = :status " +
            "AND r.expiresAt < :now")
    List<ReservationEntity> findByStatusAndExpiresAtBefore(@Param("status") ReservationStatus status,
                                                           @Param("now") LocalDateTime now
    );
}