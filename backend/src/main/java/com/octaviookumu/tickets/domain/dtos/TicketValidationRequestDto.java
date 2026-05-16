package com.octaviookumu.tickets.domain.dtos;

import com.octaviookumu.tickets.domain.entities.TicketValidationMethodEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketValidationRequestDto {
    private UUID id;
    private TicketValidationMethodEnum method;
}
