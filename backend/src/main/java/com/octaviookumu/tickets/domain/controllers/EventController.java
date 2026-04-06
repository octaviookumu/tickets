package com.octaviookumu.tickets.domain.controllers;

import com.octaviookumu.tickets.domain.CreateEventRequest;
import com.octaviookumu.tickets.domain.UpdateEventRequest;
import com.octaviookumu.tickets.domain.dtos.*;
import com.octaviookumu.tickets.domain.entities.Event;
import com.octaviookumu.tickets.mappers.EventMapper;
import com.octaviookumu.tickets.services.EventService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/events")
@RequiredArgsConstructor
public class EventController {

    private final EventMapper eventMapper;
    private final EventService eventService;


    /**
     * @param jwt                   JWT token passed by the caller
     * @param createEventRequestDto request payload
     *                              <p>
     *                              The DTO is converted to a service-layer request using
     *                              {@code eventMapper.fromDto(createEventRequestDto)}.
     * @return response
     */
    @PostMapping
    public ResponseEntity<CreateEventResponseDto> createEvent(
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody CreateEventRequestDto createEventRequestDto
    ) {
        CreateEventRequest createEventRequest = eventMapper.fromDto(createEventRequestDto);
        UUID userId = parseUserId(jwt);
        Event createdEvent = eventService.createEvent(userId, createEventRequest);
        CreateEventResponseDto createEventResponseDto = eventMapper.toDto(createdEvent);
        return new ResponseEntity<>(createEventResponseDto, HttpStatus.CREATED);
    }

    @PutMapping("/{eventId}")
    public ResponseEntity<UpdateEventResponseDto> updateEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId,
            @Valid @RequestBody UpdateEventRequestDto updateEventRequestDto
    ) {
        UpdateEventRequest updateEventRequest = eventMapper.fromDto(updateEventRequestDto);
        UUID userId = parseUserId(jwt);
        Event updatedEvent = eventService.updateEventForOrganizer(userId, eventId, updateEventRequest);
        UpdateEventResponseDto updateEventResponseDto = eventMapper.toUpdateEventResponseDto(updatedEvent);
        return ResponseEntity.ok(updateEventResponseDto);
    }

    /**
     * @param jwt      we get the id. checking authentication (who the user is)
     *                 not authorization (what they are allowed access to)
     * @param pageable one of the occasions where it goes across the different layers
     * @return listEventResponse
     */
    @GetMapping
    public ResponseEntity<Page<ListEventResponseDto>> listEvents(
            @AuthenticationPrincipal Jwt jwt, Pageable pageable
    ) {
        UUID userId = parseUserId(jwt);
        Page<Event> events = eventService.listEventsForOrganizer(userId, pageable);
        Page<ListEventResponseDto> listEventResponse = events.map(eventMapper::toListEventResponseDto);
        return ResponseEntity.ok(listEventResponse);
    }

    @GetMapping("/{eventId}")
    public ResponseEntity<GetEventDetailsResponseDto> getEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ) {
        UUID organizerId = parseUserId(jwt);
        Event event = eventService.getEventForOrganizer(organizerId, eventId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        return ResponseEntity.ok(eventMapper.toGetEventDetailsResponseDto(event));

        // I could have used the below code, but I still don't understand lambda functions that well
        // return eventService.getEventForOrganizer(organizerId, eventId)
        //        .map(eventMapper::toGetEventDetailsResponseDto)
        //        .map(ResponseEntity::ok)
        //        .orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{eventId}")
    public ResponseEntity<Void> deleteEvent(
            @AuthenticationPrincipal Jwt jwt,
            @PathVariable UUID eventId
    ) {
        UUID organizerId = parseUserId(jwt);
        eventService.deleteEventForOrganizer(organizerId, eventId);
        return ResponseEntity.noContent().build();
    }

    private UUID parseUserId(Jwt jwt) {
        return UUID.fromString(jwt.getSubject());
    }

}
