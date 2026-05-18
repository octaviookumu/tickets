package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.entities.TicketValidation;

import java.util.UUID;

public interface TicketValidationService {
    TicketValidation validateTicketByQrCode(UUID qrCodeId); // scanning the QR code

    TicketValidation validateTicketManually(UUID ticketId); // entering the ticket id manually
}
