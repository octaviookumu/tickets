package com.octaviookumu.tickets.services.impl;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.entities.Event;
import com.octaviookumu.tickets.domain.entities.TicketType;
import com.octaviookumu.tickets.domain.entities.User;
import com.octaviookumu.tickets.exceptions.UserNotFoundException;
import com.octaviookumu.tickets.repositories.EventRepository;
import com.octaviookumu.tickets.repositories.UserRepository;
import com.octaviookumu.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID '%s' not found", organizerId)
                ));

        // get ticketTypes
        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    return ticketTypeToCreate;
                }).toList();

        Event eventToCreate = new Event();
        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart().toLocalDate());
        eventToCreate.setEnd(event.getEnd().toLocalDate());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart().toLocalDate());
        eventToCreate.setSalesEnd(event.getSalesEnd().toLocalDate());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate); // we can rely on any cascades to create those for us
        return eventRepository.save(eventToCreate);
    }
}
