package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.UpdateEventRequest;
import com.octaviookumu.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;
import java.util.UUID;

/**
 * Manages events - their entire lifecycle
 */
public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);

    Optional<Event> getEventForOrganizer(UUID organizerId, UUID id);

    Event updateEventForOrganizer(UUID organizerId, UUID existingEventId, UpdateEventRequest event);

    void deleteEventForOrganizer(UUID organizerId, UUID eventId);
}
