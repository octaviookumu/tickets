package com.octaviookumu.tickets.domain.controllers;

import com.octaviookumu.tickets.domain.dtos.ListPublishedEventResponseDto;
import com.octaviookumu.tickets.domain.entities.Event;
import com.octaviookumu.tickets.mappers.EventMapper;
import com.octaviookumu.tickets.services.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "api/v1/published-events")
@RequiredArgsConstructor
public class PublishedEventController {

    private final EventMapper eventMapper;
    private final EventService eventService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedEventResponseDto>> listPublishedEvents(Pageable pageable) {
        Page<Event> events = eventService.listPublishedEvents(pageable);
        Page<ListPublishedEventResponseDto> publishedEventsResponseDto = events
                .map(eventMapper::toListPublishedEventResponseDto);
        return ResponseEntity.ok(publishedEventsResponseDto); // returns a 200
    }
}
