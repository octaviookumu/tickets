package com.octaviookumu.tickets.mappers;


import com.octaviookumu.tickets.domain.dtos.ListTicketResponseDto;
import com.octaviookumu.tickets.domain.dtos.ListTicketResponseTicketTypeDto;
import com.octaviookumu.tickets.domain.entities.Ticket;
import com.octaviookumu.tickets.domain.entities.TicketType;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TicketMapper {

    ListTicketResponseTicketTypeDto toListTicketResponseTicketTypeDto(TicketType ticketType);

    ListTicketResponseDto toListTicketResponseDto(Ticket ticket);

}
