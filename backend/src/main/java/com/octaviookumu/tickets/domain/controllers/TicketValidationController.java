package com.octaviookumu.tickets.domain.controllers;

import com.octaviookumu.tickets.domain.dtos.TicketValidationRequestDto;
import com.octaviookumu.tickets.domain.dtos.TicketValidationResponseDto;
import com.octaviookumu.tickets.domain.entities.TicketValidation;
import com.octaviookumu.tickets.domain.entities.TicketValidationMethodEnum;
import com.octaviookumu.tickets.mappers.TicketValidationMapper;
import com.octaviookumu.tickets.services.TicketValidationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/ticket-validations")
@RequiredArgsConstructor
public class TicketValidationController {

    private final TicketValidationService ticketValidationService;
    private final TicketValidationMapper ticketValidationMapper;

    // could use a different endpoint for the manual and qr scan validation
    @PostMapping()
    public ResponseEntity<TicketValidationResponseDto> validateTicket(
            @RequestBody TicketValidationRequestDto ticketValidationRequestDto
    ) {
        TicketValidationMethodEnum method = ticketValidationRequestDto.getMethod();
        TicketValidation ticketValidation;
        if (TicketValidationMethodEnum.MANUAL.equals(method)) {
            ticketValidation = ticketValidationService
                    .validateTicketManually(ticketValidationRequestDto.getId());
        } else {
            ticketValidation = ticketValidationService
                    .validateTicketByQrCode(ticketValidationRequestDto.getId());
        }

        return ResponseEntity.ok(
                ticketValidationMapper.toTicketValidationResponseDto(ticketValidation)
        );
    }
}
