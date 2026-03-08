package com.octaviookumu.tickets.domain.dtos;


import com.octaviookumu.tickets.domain.entities.EventStatusEnum;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * CreateEventRequestDto is used in the presentation layer
 * Represents exactly what the REST api expects
 * This means changes to the CreateEventRequest don't have to impact CreateEventRequestDto and vise versa
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequestDto {

    @NotBlank(message = "Event name is required")
    private String name;

    private LocalDateTime start;

    private LocalDateTime end;

    @NotBlank(message = "Venue information is required")
    private String venue;

    private LocalDateTime salesStart;

    private LocalDateTime salesEnd;

    @NotNull(message = "Event status must be provided")
    private EventStatusEnum status;

    @NotEmpty(message = "At least one ticket type is required")
    @Valid // activates any validation annotations within CreateTicketTypeRequestDto nested class
    private List<CreateTicketTypeRequestDto> ticketTypes;
}
