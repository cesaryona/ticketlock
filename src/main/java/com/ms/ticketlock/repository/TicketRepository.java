package com.ms.ticketlock.repository;

import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.repository.entity.TicketEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TicketRepository extends JpaRepository<TicketEntity, UUID> {

    Page<TicketEntity> findByEvent_EventId(UUID eventId, Pageable pageable);

    Page<TicketEntity> findByEvent_EventIdAndStatus(UUID eventId, TicketStatus status, Pageable pageable);
}