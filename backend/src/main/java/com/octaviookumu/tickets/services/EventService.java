package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.entities.Event;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.UUID;

/**
 * Manages events - their entire lifecycle
 */
public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);

    Page<Event> listEventsForOrganizer(UUID organizerId, Pageable pageable);
}
