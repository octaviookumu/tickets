package com.octaviookumu.tickets.domain;

import com.octaviookumu.tickets.domain.entities.EventStatusEnum;
import com.octaviookumu.tickets.domain.entities.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data // safe to use because there won't be any bidirectional relationships
@AllArgsConstructor
@NoArgsConstructor
public class CreateEventRequest {

    private String name;
    private LocalDateTime start;
    private LocalDateTime end;
    private String venue;
    private LocalDateTime salesStart;
    private LocalDateTime salesEnd;
    private EventStatusEnum status;
    private User organizer; // could pass something similar to CreateTicketTypeRequest
    // but no need. As the user will be passed in from the user. ie The caller of the REST API
    private List<CreateTicketTypeRequest> ticketTypes = new ArrayList<>();
}
