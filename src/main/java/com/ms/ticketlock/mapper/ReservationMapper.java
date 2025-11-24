package com.ms.ticketlock.mapper;

import com.ms.ticketlock.controller.request.ReservationRequest;
import com.ms.ticketlock.controller.response.ReservationResponse;
import com.ms.ticketlock.enums.ReservationStatus;
import com.ms.ticketlock.repository.entity.ReservationEntity;
import com.ms.ticketlock.repository.entity.TicketEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {LocalDateTime.class, ReservationStatus.class}
)
public interface ReservationMapper {

    @Mapping(target = "reservationId", ignore = true)
    @Mapping(target = "ticket", source = "ticket")
    @Mapping(target = "userId", source = "request.userId")
    @Mapping(target = "status", expression = "java(ReservationStatus.PENDING)")
    @Mapping(target = "expiresAt", expression = "java(LocalDateTime.now().plusMinutes(expirationMinutes))")
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    ReservationEntity toEntity(ReservationRequest request, TicketEntity ticket, int expirationMinutes);

    @Mapping(source = "ticket.ticketId", target = "ticketId")
    ReservationResponse toResponse(ReservationEntity entity);
}