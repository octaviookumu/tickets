package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.entities.Event;

import java.util.UUID;

/**
 * Manages events - their entire lifecycle
 */
public interface EventService {
    Event createEvent(UUID organizerId, CreateEventRequest event);
}
