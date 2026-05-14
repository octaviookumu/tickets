package com.octaviookumu.tickets.services;

import com.octaviookumu.tickets.domain.entities.QrCode;
import com.octaviookumu.tickets.domain.entities.Ticket;

import java.util.UUID;

public interface QrCodeService {
    QrCode generateQrCode(Ticket ticket);

    byte[] getQrCodeImageForUserAndTicket(UUID userId, UUID ticketId);
}
