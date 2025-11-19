package com.ms.ticketlock.mapper;

import com.ms.ticketlock.controller.request.TicketRequest;
import com.ms.ticketlock.controller.response.TicketResponse;
import com.ms.ticketlock.enums.TicketStatus;
import com.ms.ticketlock.repository.entity.EventEntity;
import com.ms.ticketlock.repository.entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {LocalDateTime.class, TicketStatus.class}
)
public interface TicketMapper {

    @Mapping(source = "event.eventId", target = "eventId")
    TicketResponse toResponse(TicketEntity entity);

    @Mapping(target = "ticketId", ignore = true)
    @Mapping(target = "event", source = "event")
    @Mapping(target = "seatRow", source = "request.seatRow")
    @Mapping(target = "seatNumber", source = "request.seatNumber")
    @Mapping(target = "status", constant = "AVAILABLE")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    TicketEntity toEntity(TicketRequest request, EventEntity event);
}
