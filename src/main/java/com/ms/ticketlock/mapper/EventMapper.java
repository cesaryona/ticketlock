package com.ms.ticketlock.mapper;

import com.ms.ticketlock.controller.request.EventRequest;
import com.ms.ticketlock.controller.response.EventResponse;
import com.ms.ticketlock.repository.entity.EventEntity;
import org.mapstruct.MapMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;

import java.time.LocalDateTime;

@Mapper(
        componentModel = MappingConstants.ComponentModel.SPRING,
        imports = {LocalDateTime.class}
)
public interface EventMapper {

    @Mapping(target = "eventId", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    EventEntity toEntity(EventRequest eventRequest);

    EventResponse toResponse(EventEntity eventEntity);

}
