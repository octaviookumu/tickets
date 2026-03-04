package com.octaviookumu.tickets.mappers;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.CreateTicketTypeRequest;
import com.octaviookumu.tickets.domain.dtos.CreateEventRequestDto;
import com.octaviookumu.tickets.domain.dtos.CreateEventResponseDto;
import com.octaviookumu.tickets.domain.dtos.CreateTicketTypeRequestDto;
import com.octaviookumu.tickets.domain.entities.Event;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;
// ignore anything you can't map

/**
 * The {@code unmappedTargetPolicy = ReportingPolicy.IGNORE} configuration
 * tells MapStruct to ignore any target properties that are not explicitly
 * mapped. No compilation error or warning will be generated for unmapped fields.
 */
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    CreateTicketTypeRequest fromDto(CreateTicketTypeRequestDto dto);

    CreateEventRequest fromDto(CreateEventRequestDto dto);

    CreateEventResponseDto toDto(Event event);

}
