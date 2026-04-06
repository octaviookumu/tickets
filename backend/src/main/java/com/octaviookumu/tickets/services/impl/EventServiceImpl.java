package com.octaviookumu.tickets.services.impl;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.UpdateEventRequest;
import com.octaviookumu.tickets.domain.UpdateTicketTypeRequest;
import com.octaviookumu.tickets.domain.entities.Event;
import com.octaviookumu.tickets.domain.entities.TicketType;
import com.octaviookumu.tickets.domain.entities.User;
import com.octaviookumu.tickets.exceptions.EventNotFoundException;
import com.octaviookumu.tickets.exceptions.EventUpateException;
import com.octaviookumu.tickets.exceptions.TicketTypeNotFoundException;
import com.octaviookumu.tickets.exceptions.UserNotFoundException;
import com.octaviookumu.tickets.repositories.EventRepository;
import com.octaviookumu.tickets.repositories.UserRepository;
import com.octaviookumu.tickets.services.EventService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

    private final UserRepository userRepository;
    private final EventRepository eventRepository;

    @Override
    @Transactional
    /*
     * To prevent inconsistent state especially in high traffic situations
     */
    public Event createEvent(UUID organizerId, CreateEventRequest event) {
        User organizer = userRepository.findById(organizerId)
                .orElseThrow(() -> new UserNotFoundException(
                        String.format("User with ID '%s' not found", organizerId)
                ));

        Event eventToCreate = new Event();

        // get ticketTypes
        List<TicketType> ticketTypesToCreate = event.getTicketTypes().stream().map(
                ticketType -> {
                    TicketType ticketTypeToCreate = new TicketType();
                    ticketTypeToCreate.setName(ticketType.getName());
                    ticketTypeToCreate.setPrice(ticketType.getPrice());
                    ticketTypeToCreate.setDescription(ticketType.getDescription());
                    ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                    ticketTypeToCreate.setEvent(eventToCreate);
                    return ticketTypeToCreate;
                }).toList();


        eventToCreate.setName(event.getName());
        eventToCreate.setStart(event.getStart());
        eventToCreate.setEnd(event.getEnd());
        eventToCreate.setVenue(event.getVenue());
        eventToCreate.setSalesStart(event.getSalesStart());
        eventToCreate.setSalesEnd(event.getSalesEnd());
        eventToCreate.setStatus(event.getStatus());
        eventToCreate.setOrganizer(organizer);
        eventToCreate.setTicketTypes(ticketTypesToCreate); // we can rely on any cascades to create those for us
        return eventRepository.save(eventToCreate);
    }

    @Override
    public Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable) {
        return eventRepository.findByOrganizerId(organizerId, pageable);
    }

    @Override
    public Optional<Event> getEventForOrganizer(UUID organizerId, UUID id) {
        return eventRepository.findByIdAndOrganizerId(id, organizerId);
    }

    @Override
    @Transactional
    /*
     * we're making multiple calls to the database.
     * we want this all to happen in the same transaction to the db doesn't get into an inconsistent state
     */
    public Event updateEventForOrganizer(UUID organizerId, UUID existingEventId, UpdateEventRequest event) {
        if (event.getId() == null) {
            throw new EventUpateException("Event ID cannot be null");
        }

        if (!existingEventId.equals(event.getId())) {
            throw new EventUpateException("Cannot update the ID of an event");
        }

        Event existingEvent = eventRepository.findByIdAndOrganizerId(existingEventId, organizerId)
                .orElseThrow(() -> new EventNotFoundException(
                        String.format("Event with ID '%s' does not exist", existingEventId)
                ));

        existingEvent.setName(event.getName());
        existingEvent.setStart(event.getStart());
        existingEvent.setEnd(event.getEnd());
        existingEvent.setVenue(event.getVenue());
        existingEvent.setSalesStart(event.getSalesStart());
        existingEvent.setSalesEnd(event.getSalesEnd());
        existingEvent.setStatus(event.getStatus());

        // get all ids provided in event argument
        // set of all the ids of the ticket types we want to create excluding any nulls
        Set<UUID> requestTicketTypeIds = event.getTicketTypes()
                .stream()
                .map(UpdateTicketTypeRequest::getId) // “For each UpdateTicketTypeRequest object, call its getId() method”
                .filter(Objects::nonNull)  // filter out null ids
                .collect(Collectors.toSet());

        // remove any ticket types from the existing event where the id isn't in requestTicketTypeIds
        // remove all ticket types not in the request
        existingEvent.getTicketTypes().removeIf((existingTicketType) ->
                !requestTicketTypeIds.contains(existingTicketType.getId())
        );

        // create and index of all the existing ticket types by their ids
        // a map where id is the id of the ticket type, the value is the ticket type
        Map<UUID, TicketType> existingTicketTypesIndex = existingEvent.getTicketTypes().stream()
                .collect(Collectors.toMap(TicketType::getId, Function.identity()));

        for (UpdateTicketTypeRequest ticketType : event.getTicketTypes()) {
            if (ticketType.getId() == null) {
                // Create
                TicketType ticketTypeToCreate = new TicketType();
                ticketTypeToCreate.setName(ticketType.getName());
                ticketTypeToCreate.setPrice(ticketType.getPrice());
                ticketTypeToCreate.setDescription(ticketType.getDescription());
                ticketTypeToCreate.setTotalAvailable(ticketType.getTotalAvailable());
                ticketTypeToCreate.setEvent(existingEvent); // make sure bidirectional relationship is set up
                existingEvent.getTicketTypes().add(ticketTypeToCreate);
            } else if (existingTicketTypesIndex.containsKey(ticketType.getId())) {
                // Update
                TicketType existingTicketTYpe = existingTicketTypesIndex.get(ticketType.getId());
                existingTicketTYpe.setName(ticketType.getName());
                existingTicketTYpe.setPrice(ticketType.getPrice());
                existingTicketTYpe.setDescription(ticketType.getDescription());
                existingTicketTYpe.setTotalAvailable(ticketType.getTotalAvailable());

            } else {
                // we have an id of a ticket type that doesn't exist
                throw new TicketTypeNotFoundException(
                        String.format("Ticket type with ID '%s' does not exist", ticketType.getId())
                );

            }
        }

        return eventRepository.save(existingEvent);

    }

    @Override
    @Transactional // as we're making calls to the database
    // NOTE: This method will not throw an exception if the organizer doesn't have access to the event
    public void deleteEventForOrganizer(UUID organizerId, UUID eventId) {
        getEventForOrganizer(organizerId, eventId).ifPresent(eventRepository::delete);
    }
}
