package com.octaviookumu.tickets.mappers;

import com.octaviookumu.tickets.domain.dtos.TicketValidationResponseDto;
import com.octaviookumu.tickets.domain.entities.TicketValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketValidationMapper {

    // because the ticketValidation doesn't have the concept of a ticket id
    // it does have the concept of a ticket with an id, we need to add a mapping

    // the target is ticketId which is the instance variable on the TicketValidationResponseDto
    @Mapping(target = "ticketId", source = "ticket.id")
    TicketValidationResponseDto toTicketValidationResponseDto(TicketValidation ticketValidation);
}
